package com.example.miaplicacion

import android.app.Application

class HelpingApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SessionManager.init(this)

        NotificacionHelper.crearCanales(this)

        FirebaseManager.init(this)
        FirestoreSyncHelper.init()
    }
}
