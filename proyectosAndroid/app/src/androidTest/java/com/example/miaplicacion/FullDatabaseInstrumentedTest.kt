package com.example.miaplicacion

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FullDatabaseInstrumentedTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun actualizarMedicacion_modificaCampos() = runBlocking {
        val med = Medicacion(nombre = "Original", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 1)
        val id = db.medicacionDao().insert(med)
        val modificada = med.copy(id = id.toInt(), nombre = "Modificado", dosis = "2")
        db.medicacionDao().update(modificada)
        val recuperada = db.medicacionDao().getById(id.toInt())
        assertEquals("Modificado", recuperada?.nombre)
        assertEquals("2", recuperada?.dosis)
    }

    @Test
    fun obtenerMedicacionesPorUsuario_filtraCorrectamente() = runBlocking {
        val med1 = Medicacion(nombre = "Med1", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 1)
        val med2 = Medicacion(nombre = "Med2", dosis = "1", frecuencia = "diaria", hora = "20:00", userId = 1)
        val med3 = Medicacion(nombre = "Med3", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 2)
        db.medicacionDao().insert(med1)
        db.medicacionDao().insert(med2)
        db.medicacionDao().insert(med3)
        val deUser1 = db.medicacionDao().getByUserId(1).first()
        assertEquals(2, deUser1.size)
    }

    @Test
    fun eliminarRegistroSalud_liberaEspacio() = runBlocking {
        val r = RegistroSalud(tipo = "glucosa", valor = "95", fecha = "01/01/2026", userId = 1)
        val id = db.registroSaludDao().insert(r)
        val antes = db.registroSaludDao().getAllByUserId(1).first().size
        db.registroSaludDao().delete(db.registroSaludDao().getById(id.toInt())!!)
        val despues = db.registroSaludDao().getAllByUserId(1).first().size
        assertEquals(antes - 1, despues)
    }

    @Test
    fun eventosPorTipo_filtraCorrectamente() = runBlocking {
        val e1 = EventoCalendario(titulo = "Cita médica", fecha = "01/06/2026", tipo = "cita_medica", userId = 1)
        val e2 = EventoCalendario(titulo = "Cumpleaños", fecha = "15/06/2026", tipo = "cumpleanos", userId = 1)
        db.eventoCalendarioDao().insert(e1)
        db.eventoCalendarioDao().insert(e2)
        val eventosMedicos = db.eventoCalendarioDao().getAllByUserId(1).first()
            .filter { it.tipo == "cita_medica" }
        assertEquals(1, eventosMedicos.size)
    }

    @Test
    fun miembroFamiliar_cambiarRol_actualizaPermisos() = runBlocking {
        val m = MiembroFamiliar(nombre = "Test", email = "t@t.com", rol = "viewer", mayorId = 1, userId = 1)
        val id = db.miembroFamiliarDao().insert(m)
        val modificado = m.copy(id = id.toInt(), rol = "admin")
        db.miembroFamiliarDao().update(modificado)
        val recuperado = db.miembroFamiliarDao().getById(id.toInt())
        assertEquals("admin", recuperado?.rol)
    }

    @Test
    fun flujoCompletoInsertsMultiples() = runBlocking {
        // Medicacion
        val mid = db.medicacionDao().insert(Medicacion(nombre = "Test", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 1))
        assertTrue(mid > 0)
        // Contacto
        val cid = db.contactoEmergenciaDao().insert(ContactoEmergencia(nombre = "C", telefono = "123", userId = 1))
        assertTrue(cid > 0)
        // Salud
        val sid = db.registroSaludDao().insert(RegistroSalud(tipo = "peso", valor = "75", fecha = "01/01/2026", userId = 1))
        assertTrue(sid > 0)
        // Evento
        val eid = db.eventoCalendarioDao().insert(EventoCalendario(titulo = "E", fecha = "01/01/2026", userId = 1))
        assertTrue(eid > 0)
        // Familiar
        val fid = db.miembroFamiliarDao().insert(MiembroFamiliar(nombre = "F", email = "f@f.com", mayorId = 1, userId = 1))
        assertTrue(fid > 0)
        // Verify all retrievable
        assertNotNull(db.medicacionDao().getById(mid.toInt()))
        assertNotNull(db.contactoEmergenciaDao().getById(cid.toInt()))
        assertNotNull(db.registroSaludDao().getById(sid.toInt()))
        assertNotNull(db.eventoCalendarioDao().getById(eid.toInt()))
        assertNotNull(db.miembroFamiliarDao().getById(fid.toInt()))
    }

    @Test
    fun eliminarPorUsuario_mantieneDatosDeOtros() = runBlocking {
        val m1 = Medicacion(nombre = "User1Med", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 1)
        val m2 = Medicacion(nombre = "User2Med", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 2)
        db.medicacionDao().insert(m1)
        db.medicacionDao().insert(m2)
        val deUser1 = db.medicacionDao().getByUserId(1).first()
        db.medicacionDao().delete(deUser1[0])
        val deUser2 = db.medicacionDao().getByUserId(2).first()
        assertEquals(1, deUser2.size)
    }
}
