package com.example.miaplicacion

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseManager {
    private const val TAG = "FirebaseManager"
    var disponible: Boolean = false
        private set

    private var auth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null

    fun init(context: Context) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            auth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()
            disponible = true
            Log.d(TAG, "Firebase inicializado correctamente")
        } catch (e: Exception) {
            disponible = false
            Log.w(TAG, "Firebase no disponible. Modo offline.", e)
        }
    }

    fun getAuth(): FirebaseAuth? = auth
    fun getFirestore(): FirebaseFirestore? = firestore

    fun registrarUsuario(email: String, password: String): Task<AuthResult>? {
        return auth?.createUserWithEmailAndPassword(email, password)
    }

    fun iniciarSesion(email: String, password: String): Task<AuthResult>? {
        return auth?.signInWithEmailAndPassword(email, password)
    }

    fun cerrarSesion() {
        auth?.signOut()
    }

    fun obtenerTokenFCM(onToken: (String?) -> Unit) {
        if (!disponible) {
            onToken(null)
            return
        }
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token -> onToken(token) }
            .addOnFailureListener { onToken(null) }
    }
}
