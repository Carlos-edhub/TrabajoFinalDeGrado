package com.example.miaplicacion

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "session_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(user: UserEntity) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.nombre)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_ROLE, user.rol)
            apply()
        }
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserRole(): String = prefs.getString(KEY_USER_ROLE, "usuario") ?: "usuario"

    fun isLoggedIn(): Boolean = prefs.contains(KEY_USER_ID) && getUserId() > 0

    fun logout() {
        FirebaseManager.cerrarSesion()
        prefs.edit().clear().apply()
    }
}
