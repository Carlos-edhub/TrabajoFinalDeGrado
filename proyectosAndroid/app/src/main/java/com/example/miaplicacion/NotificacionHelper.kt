package com.example.miaplicacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificacionHelper {
    const val CHANNEL_MEDICACION = "medicacion_channel"
    const val CHANNEL_SOS = "sos_channel"
    const val CHANNEL_CALENDARIO = "calendario_channel"

    fun crearCanales(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canales = listOf(
                NotificationChannel(
                    CHANNEL_MEDICACION, "Recordatorio de medicación",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Recordatorios para tomar medicamentos"
                },
                NotificationChannel(
                    CHANNEL_SOS, "Emergencia SOS",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Alertas de emergencia"
                },
                NotificationChannel(
                    CHANNEL_CALENDARIO, "Calendario",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Recordatorios de eventos del calendario"
                }
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            canales.forEach { manager.createNotificationChannel(it) }
        }
    }

    fun mostrarNotificacion(context: Context, titulo: String, mensaje: String, canal: String = CHANNEL_MEDICACION) {
        val notif = NotificationCompat.Builder(context, canal)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notif)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
