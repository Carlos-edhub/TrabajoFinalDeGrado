package com.example.miaplicacion

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MedicacionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val nombre = inputData.getString("nombre") ?: return Result.success()
        val dosis = inputData.getString("dosis") ?: ""
        val medicacionId = inputData.getInt("medicacion_id", 0)

        val db = AppDatabase.getInstance(applicationContext)
        val med = db.medicacionDao().getById(medicacionId)
        if (med == null || !med.activo) return Result.success()

        NotificacionHelper.crearCanales(applicationContext)
        NotificacionHelper.mostrarNotificacion(
            context = applicationContext,
            titulo = "Recordatorio de medicación",
            mensaje = "Toca tomar: $nombre${if (dosis.isNotEmpty()) " ($dosis)" else ""}"
        )

        return Result.success()
    }

    companion object {
        const val NOMBRE_MED = "nombre"
        const val DOSIS_MED = "dosis"
        const val ID_MED = "medicacion_id"
    }
}
