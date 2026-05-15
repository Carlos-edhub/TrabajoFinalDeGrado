package com.example.miaplicacion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val db = AppDatabase.getInstance(context)
            Thread {
                val allMedicaciones = mutableListOf<Medicacion>()
                runBlocking {
                    val users = db.userDao().getAll().first()
                    for (user in users) {
                        val meds = db.medicacionDao().getByUserId(user.id).first()
                        allMedicaciones.addAll(meds)
                    }
                }
                for (med in allMedicaciones) {
                    if (med.activo && med.hora.isNotEmpty()) {
                        MedicacionWorkScheduler.programar(context, med.id, med.nombre, med.dosis, med.hora)
                    }
                }
            }.start()
        }
    }
}
