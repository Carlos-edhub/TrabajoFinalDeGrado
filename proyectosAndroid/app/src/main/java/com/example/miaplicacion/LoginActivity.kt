package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityLoginBinding
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        binding.btnLogin.setOnClickListener {
            iniciarSesion()
        }

        binding.txtIrARegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun iniciarSesion() {
        val email = binding.txtEmail.text?.toString()?.trim() ?: ""
        val password = binding.txtPassword.text?.toString()?.trim() ?: ""

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        Thread {
            val user = runBlocking { db.userDao().login(email, password) }
            runOnUiThread {
                if (user != null) {
                    SessionManager.saveSession(user)

                    if (FirebaseManager.disponible) {
                        FirebaseManager.iniciarSesion(email, password)?.addOnFailureListener { e ->
                            android.util.Log.w("Login", "Firebase Auth login falló (no crítico)", e)
                        }
                        FirebaseManager.obtenerTokenFCM { token ->
                            if (token != null) {
                                android.util.Log.d("Login", "FCM token: $token")
                            }
                        }
                    }

                    Toast.makeText(this, "Bienvenido, ${user.nombre}", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}
