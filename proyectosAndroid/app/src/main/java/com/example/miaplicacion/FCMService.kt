package com.example.miaplicacion

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo token FCM: $token")
        guardarTokenEnFirestore(token)
    }

    private fun guardarTokenEnFirestore(token: String) {
        if (!FirebaseManager.disponible) return
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        val data = hashMapOf("fcmToken" to token, "userId" to userId)
        FirebaseManager.getFirestore()
            ?.collection("fcm_tokens")
            ?.document("user_$userId")
            ?.set(data)
            ?.addOnSuccessListener { Log.d(TAG, "Token FCM guardado en Firestore") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error guardando token FCM", e) }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "Mensaje recibido: ${message.from}")

        message.notification?.let { notif ->
            mostrarNotificacion(
                titulo = notif.title ?: "Helping a GrandPa!",
                cuerpo = notif.body ?: "",
                tipo = message.data["tipo"] ?: ""
            )
        }

        if (message.data.isNotEmpty()) {
            val titulo = message.data["titulo"] ?: "Helping a GrandPa!"
            val cuerpo = message.data["cuerpo"] ?: ""
            val tipo = message.data["tipo"] ?: ""
            mostrarNotificacion(titulo, cuerpo, tipo)
        }
    }

    private fun mostrarNotificacion(titulo: String, cuerpo: String, tipo: String) {
        val canal = when (tipo) {
            "medicacion" -> NotificacionHelper.CHANNEL_MEDICACION
            "sos" -> NotificacionHelper.CHANNEL_SOS
            "calendario" -> NotificacionHelper.CHANNEL_CALENDARIO
            else -> NotificacionHelper.CHANNEL_MEDICACION
        }

        NotificacionHelper.crearCanales(this)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(this, canal)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(System.currentTimeMillis().toInt(), notificacion)
        } catch (e: Exception) {
            Log.e(TAG, "Error mostrando notificación FCM", e)
        }
    }

    companion object {
        private const val TAG = "FCMService"
    }
}
