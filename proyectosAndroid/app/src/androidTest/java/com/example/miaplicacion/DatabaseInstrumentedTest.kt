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
class DatabaseInstrumentedTest {

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
    fun insertarYRecuperarMedicacion() = runBlocking {
        val med = Medicacion(nombre = "TestMed", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 1)
        val id = db.medicacionDao().insert(med)
        assertTrue(id > 0)

        val recuperada = db.medicacionDao().getById(id.toInt())
        assertNotNull(recuperada)
        assertEquals("TestMed", recuperada?.nombre)
    }

    @Test
    fun insertarYRecuperarContactoEmergencia() = runBlocking {
        val c = ContactoEmergencia(nombre = "Contacto Test", telefono = "600000000", userId = 1)
        val id = db.contactoEmergenciaDao().insert(c)
        assertTrue(id > 0)

        val recuperado = db.contactoEmergenciaDao().getById(id.toInt())
        assertNotNull(recuperado)
        assertEquals("Contacto Test", recuperado?.nombre)
    }

    @Test
    fun insertarYRecuperarRegistroSalud() = runBlocking {
        val r = RegistroSalud(tipo = "peso", valor = "75kg", fecha = "01/01/2026", userId = 1)
        val id = db.registroSaludDao().insert(r)
        assertTrue(id > 0)

        val recuperado = db.registroSaludDao().getById(id.toInt())
        assertNotNull(recuperado)
        assertEquals("75kg", recuperado?.valor)
    }

    @Test
    fun insertarYRecuperarEventoCalendario() = runBlocking {
        val e = EventoCalendario(titulo = "Evento Test", fecha = "01/01/2026", userId = 1)
        val id = db.eventoCalendarioDao().insert(e)
        assertTrue(id > 0)

        val recuperado = db.eventoCalendarioDao().getById(id.toInt())
        assertNotNull(recuperado)
        assertEquals("Evento Test", recuperado?.titulo)
    }

    @Test
    fun insertarYRecuperarMiembroFamiliar() = runBlocking {
        val m = MiembroFamiliar(nombre = "Familiar Test", email = "familiar@test.com", mayorId = 1, userId = 1)
        val id = db.miembroFamiliarDao().insert(m)
        assertTrue(id > 0)

        val recuperado = db.miembroFamiliarDao().getById(id.toInt())
        assertNotNull(recuperado)
        assertEquals("Familiar Test", recuperado?.nombre)
    }

    @Test
    fun eliminarMedicacion() = runBlocking {
        val med = Medicacion(nombre = "Eliminar", dosis = "1", frecuencia = "diaria", hora = "08:00", userId = 1)
        val id = db.medicacionDao().insert(med)
        val recuperada = db.medicacionDao().getById(id.toInt())
        assertNotNull(recuperada)

        db.medicacionDao().delete(recuperada!!)
        val trasEliminar = db.medicacionDao().getById(id.toInt())
        assertNull(trasEliminar)
    }

    @Test
    fun flujoCompletoContactos() = runBlocking {
        val c1 = ContactoEmergencia(nombre = "Uno", telefono = "111", userId = 1)
        val c2 = ContactoEmergencia(nombre = "Dos", telefono = "222", userId = 1)
        db.contactoEmergenciaDao().insert(c1)
        db.contactoEmergenciaDao().insert(c2)

        val todos = db.contactoEmergenciaDao().getAllByUserId(1)
        assertEquals(2, todos.size)
    }

    @Test
    fun obtenerEventosPorUsuario() = runBlocking {
        val e1 = EventoCalendario(titulo = "E1", fecha = "01/01/2026", userId = 1)
        val e2 = EventoCalendario(titulo = "E2", fecha = "02/01/2026", userId = 2)
        db.eventoCalendarioDao().insert(e1)
        db.eventoCalendarioDao().insert(e2)

        val eventosUser1 = db.eventoCalendarioDao().getByUserId(1).first()
        assertEquals(1, eventosUser1.size)
        assertEquals("E1", eventosUser1[0].titulo)
    }

    @Test
    fun miembroFamiliar_conRolCorrecto() = runBlocking {
        val m = MiembroFamiliar(nombre = "Admin", email = "a@a.com", rol = "admin", mayorId = 1, userId = 1)
        val id = db.miembroFamiliarDao().insert(m)
        val recuperado = db.miembroFamiliarDao().getById(id.toInt())
        assertEquals("admin", recuperado?.rol)
    }
}
