package com.example.miaplicacion

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmaMedicacionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val nombreMed = intent.getStringExtra("nombre_medicacion") ?: "Medicación"
        val dosis = intent.getStringExtra("dosis") ?: ""

        NotificacionHelper.crearCanales(context)
        NotificacionHelper.mostrarNotificacion(
            context = context,
            titulo = "Recordatorio de medicación",
            mensaje = "Toca tomar: $nombreMed${if (dosis.isNotEmpty()) " ($dosis)" else ""}"
        )
    }

    companion object {
        fun crearPendingIntent(context: Context, medicacionId: Int, nombre: String, dosis: String): PendingIntent {
            val intent = Intent(context, AlarmaMedicacionReceiver::class.java).apply {
                putExtra("medicacion_id", medicacionId)
                putExtra("nombre_medicacion", nombre)
                putExtra("dosis", dosis)
            }
            return PendingIntent.getBroadcast(
                context, medicacionId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
