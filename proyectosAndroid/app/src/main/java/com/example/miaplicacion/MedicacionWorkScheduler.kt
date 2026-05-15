package com.example.miaplicacion

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

object MedicacionWorkScheduler {

    fun programar(context: Context, medicacionId: Int, nombre: String, dosis: String, hora: String) {
        val partes = hora.split(":")
        if (partes.size != 2) return
        val hour = partes[0].toIntOrNull() ?: return
        val minute = partes[1].toIntOrNull() ?: return

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val delay = target.timeInMillis - now.timeInMillis

        val inputData = workDataOf(
            MedicacionWorker.ID_MED to medicacionId,
            MedicacionWorker.NOMBRE_MED to nombre,
            MedicacionWorker.DOSIS_MED to dosis
        )

        val request = OneTimeWorkRequestBuilder<MedicacionWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("medicacion_$medicacionId")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "medicacion_diaria_$medicacionId",
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

    fun cancelar(context: Context, medicacionId: Int) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("medicacion_diaria_$medicacionId")
    }

    fun reprogramarTrasNotificacion(context: Context, medicacionId: Int, nombre: String, dosis: String, hora: String) {
        programar(context, medicacionId, nombre, dosis, hora)
    }
}
