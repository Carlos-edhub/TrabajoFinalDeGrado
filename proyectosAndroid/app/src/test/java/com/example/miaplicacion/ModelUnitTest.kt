package com.example.miaplicacion

import org.junit.Assert.*
import org.junit.Test

class ModelUnitTest {

    @Test
    fun medicacion_conCamposObligatorios_esValida() {
        val med = Medicacion(nombre = "Paracetamol", dosis = "1 pastilla", frecuencia = "cada 8h", hora = "08:00")
        assertEquals("Paracetamol", med.nombre)
        assertTrue(med.activo)
        assertEquals(0, med.id)
    }

    @Test
    fun contactoEmergencia_conTelefonoYNombre_esValido() {
        val c = ContactoEmergencia(nombre = "Hijo", telefono = "612345678")
        assertEquals("Hijo", c.nombre)
        assertEquals("612345678", c.telefono)
    }

    @Test
    fun registroSalud_tieneFechaYTipo() {
        val r = RegistroSalud(tipo = "presion", valor = "120/80", fecha = "01/01/2026")
        assertEquals("presion", r.tipo)
        assertEquals("120/80", r.valor)
    }

    @Test
    fun eventoCalendario_conTituloYFecha_esValido() {
        val e = EventoCalendario(titulo = "Cita médica", fecha = "15/06/2026", tipo = "médica")
        assertEquals("Cita médica", e.titulo)
        assertEquals("médica", e.tipo)
    }

    @Test
    fun miembroFamiliar_conRolViewer_porDefecto() {
        val m = MiembroFamiliar(nombre = "Ana", email = "ana@test.com")
        assertEquals("viewer", m.rol)
    }

    @Test
    fun userEntity_conRolUsuario_porDefecto() {
        val u = UserEntity(nombre = "Test", apellidos = "User", email = "t@t.com", password = "123456")
        assertEquals("usuario", u.rol)
    }

    @Test
    fun medicacion_inactiva_noDebeSonarAlarma() {
        val med = Medicacion(nombre = "Ibuprofeno", dosis = "1 cápsula", frecuencia = "cada 12h", hora = "22:00", activo = false)
        assertFalse(med.activo)
    }

    @Test
    fun historialServicio_calculaTotalCorrectamente() {
        val h = ServicioHistorial(
            horasMedicas = 3,
            horasCompras = 2,
            totalHoras = 5,
            totalPrecio = 5 * 3.99
        )
        assertEquals(5, h.totalHoras)
        assertEquals(19.95, h.totalPrecio, 0.01)
    }

    @Test
    fun abuelito_tieneCamposObligatorios() {
        val a = Abuelito(nombre = "Pepe", apellidos = "García", edad = "78")
        assertEquals("Pepe", a.nombre)
        assertEquals("78", a.edad)
    }

    @Test
    fun trabajador_toString_devuelveNombreCompleto() {
        val t = Trabajador(nombre = "María", apellido = "López")
        assertEquals("María López", t.toString())
    }
}
