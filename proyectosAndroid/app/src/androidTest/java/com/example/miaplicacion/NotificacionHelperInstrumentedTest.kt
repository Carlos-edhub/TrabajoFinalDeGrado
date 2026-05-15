package com.example.miaplicacion

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificacionHelperInstrumentedTest {

    @Test
    fun canalesNotificacion_creadosCorrectamente() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val canales = notificationManager.notificationChannels
        assertNotNull(canales)
        val nombresCanales = canales.map { it.id }
        assertTrue(nombresCanales.contains("medicacion"))
        assertTrue(nombresCanales.contains("sos"))
        assertTrue(nombresCanales.contains("calendario"))
    }

    @Test
    fun canalMedicacion_tieneImportanciaAlta() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val canal = notificationManager.getNotificationChannel("medicacion")
        assertNotNull(canal)
        assertEquals(NotificationManager.IMPORTANCE_HIGH, canal.importance)
    }
}
