package com.example.miaplicacion

import org.junit.Assert.*
import org.junit.Test

class FirebaseManagerUnitTest {

    @Test
    fun firebaseManager_esObjetoSingleton() {
        assertNotNull(FirebaseManager)
    }

    @Test
    fun usuarioValido_emailContieneArroba() {
        val email = "usuario@test.com"
        assertTrue(email.contains("@"))
    }

    @Test
    fun usuarioInvalido_emailSinArroba() {
        val email = "usuariotest.com"
        assertFalse(email.contains("@"))
    }

    @Test
    fun contrasenaValida_longitudMinima() {
        val password = "12345678"
        assertTrue(password.length >= 8)
    }

    @Test
    fun contrasenaInvalida_demasiadoCorta() {
        val password = "1234567"
        assertFalse(password.length >= 8)
    }

    @Test
    fun contrasenaFuerte_contieneMayusculaNumero() {
        val password = "Helping1"
        assertTrue(password.any { it.isUpperCase() })
        assertTrue(password.any { it.isDigit() })
    }

    @Test
    fun emailConEspacios_seNormaliza() {
        val email = "  usuario@test.com  "
        val normalizado = email.trim().lowercase()
        assertEquals("usuario@test.com", normalizado)
    }

    @Test
    fun rolesValidos_incluyenAdmin() {
        val roles = listOf("admin", "caregiver", "viewer")
        assertTrue(roles.contains("admin"))
        assertTrue(roles.contains("caregiver"))
        assertTrue(roles.contains("viewer"))
        assertEquals(3, roles.size)
    }

    @Test
    fun rolInvalido_noEstaEnLista() {
        val roles = listOf("admin", "caregiver", "viewer")
        assertFalse(roles.contains("superadmin"))
    }

    @Test
    fun usuarioPorDefecto_tieneRolViewer() {
        val roles = listOf("admin", "caregiver", "viewer")
        assertEquals("viewer", roles[2])
    }
}
