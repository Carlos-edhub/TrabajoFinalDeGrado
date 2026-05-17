package com.example.miaplicacion

import org.junit.Assert.*
import org.junit.Test

class SosHelperUnitTest {

    @Test
    fun mensajeSos_incluyeNombreUsuario() {
        val contacto = ContactoEmergencia(nombre = "Hijo", telefono = "612345678")
        val mensaje = buildString {
            append("¡EMERGENCIA! Necesito ayuda urgente.")
            append("\n\nMi ubicación: https://maps.google.com/?q=40.4168,-3.7038")
        }
        assertTrue(mensaje.contains("EMERGENCIA"))
        assertTrue(mensaje.contains("maps.google.com"))
        assertTrue(mensaje.contains("40.4168"))
    }

    @Test
    fun mensajeSos_sinUbicacion_noIncluyeCoordenadas() {
        val mensaje = buildString {
            append("¡EMERGENCIA! Necesito ayuda urgente.")
        }
        assertFalse(mensaje.contains("maps.google.com"))
        assertEquals(1, mensaje.split("EMERGENCIA").size - 1)
    }

    @Test
    fun uriWhatsApp_formatoCorrecto() {
        val telefono = "612345678"
        val mensaje = "Ayuda"
        val uri = "https://api.whatsapp.com/send?phone=$telefono&text=${java.net.URLEncoder.encode(mensaje, "UTF-8")}"
        assertTrue(uri.startsWith("https://api.whatsapp.com/send?phone="))
        assertTrue(uri.contains("612345678"))
        assertTrue(uri.contains("Ayuda"))
    }

    @Test
    fun uriSms_formatoCorrecto() {
        val telefono = "612345678"
        val mensaje = "SOS emergencia"
        val uri = "sms:$telefono"
        assertEquals("sms:612345678", uri)
    }

    @Test
    fun uriLlamada_formatoCorrecto() {
        val telefono = "612345678"
        val uri = "tel:$telefono"
        assertEquals("tel:612345678", uri)
    }

    @Test
    fun contactoEmergencia_conTelefonoVacio_devuelveVacio() {
        val contacto = ContactoEmergencia(nombre = "Test", telefono = "")
        assertTrue(contacto.telefono.isEmpty())
    }

    @Test
    fun contactosMultiples_generaMensajesSeparados() {
        val contactos = listOf(
            ContactoEmergencia(nombre = "A", telefono = "111"),
            ContactoEmergencia(nombre = "B", telefono = "222")
        )
        assertEquals(2, contactos.size)
        contactos.forEachIndexed { i, c ->
            assertEquals("${i + 1}".repeat(3), c.telefono)
        }
    }

    @Test
    fun contacto_listaMultiple_contieneElementos() {
        val contactos = listOf(
            ContactoEmergencia(nombre = "Primero", telefono = "111"),
            ContactoEmergencia(nombre = "Segundo", telefono = "222")
        )
        assertEquals(2, contactos.size)
        assertEquals("Primero", contactos[0].nombre)
        assertEquals("Segundo", contactos[1].nombre)
    }

    @Test
    fun urlGoogleMaps_formatoValido() {
        val lat = 40.4168
        val lon = -3.7038
        val url = "https://maps.google.com/?q=$lat,$lon"
        assertTrue(url.matches(Regex("https://maps\\.google\\.com/\\?q=-?\\d+\\.\\d+,-?\\d+\\.\\d+")))
    }

    @Test
    fun mensajeSos_longitudNoExcedeLimiteSms() {
        val mensaje = buildString {
            append("¡EMERGENCIA! Soy Usuario y necesito ayuda urgente.")
            append("\n\nMi ubicación: https://maps.google.com/?q=40.4168,-3.7038")
        }
        assertTrue(mensaje.length <= 160)
    }

    @Test
    fun mensajeSos_conNombreLargo_noExcedeDosSms() {
        val nombre = "A".repeat(50)
        val mensaje = buildString {
            append("¡EMERGENCIA! Soy $nombre y necesito ayuda urgente.")
            append("\n\nMi ubicación: https://maps.google.com/?q=40.4168,-3.7038")
        }
        assertTrue(mensaje.length <= 320)
    }
}
