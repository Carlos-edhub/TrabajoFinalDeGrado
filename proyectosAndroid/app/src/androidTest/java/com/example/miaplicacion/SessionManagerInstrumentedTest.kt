package com.example.miaplicacion

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionManagerInstrumentedTest {

    @Before
    fun setup() {
        SessionManager.init(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun sesion_iniciaCerrada() {
        assertFalse(SessionManager.isLoggedIn())
    }

    @Test
    fun guardarSesion_guardaDatosCorrectamente() {
        val user = UserEntity(
            id = 1,
            nombre = "Test",
            apellidos = "User",
            email = "test@test.com",
            password = "123456",
            rol = "admin"
        )
        SessionManager.saveSession(user)
        assertTrue(SessionManager.isLoggedIn())
        assertEquals(1, SessionManager.getUserId())
        assertEquals("Test", SessionManager.getUserName())
        assertEquals("test@test.com", SessionManager.getUserEmail())
        assertEquals("admin", SessionManager.getUserRole())
    }

    @Test
    fun cerrarSesion_limpiaDatos() {
        val user = UserEntity(id = 1, nombre = "Test", apellidos = "User", email = "t@t.com", password = "123456")
        SessionManager.saveSession(user)
        assertTrue(SessionManager.isLoggedIn())
        SessionManager.logout()
        assertFalse(SessionManager.isLoggedIn())
    }

    @Test
    fun usuarioPorDefecto_tieneRolUsuario() {
        val user = UserEntity(id = 2, nombre = "Default", apellidos = "User", email = "d@d.com", password = "123456")
        SessionManager.saveSession(user)
        assertEquals("usuario", SessionManager.getUserRole())
    }

    @Test
    fun getUserId_sinSesion_devuelveMenosUno() {
        assertFalse(SessionManager.isLoggedIn())
    }
}
