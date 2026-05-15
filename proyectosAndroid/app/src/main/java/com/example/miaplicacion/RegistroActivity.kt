package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityRegistroBinding
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registro"

        db = AppDatabase.getInstance(this)

        binding.btnConfirmar.setOnClickListener {
            confirmarRegistro()
        }

        binding.txtIrATrabajador.setOnClickListener {
            startActivity(Intent(this, TrabajoActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun confirmarRegistro() {
        val nombre = binding.txtNombre.text?.toString()?.trim() ?: ""
        val apellidos = binding.txtApellidos.text?.toString()?.trim() ?: ""
        val email = binding.txtEmail.text?.toString()?.trim() ?: ""
        val password = binding.txtPassword.text?.toString()?.trim() ?: ""
        val telefono = binding.txtTelefono.text?.toString()?.trim() ?: ""
        val direccion = binding.txtDireccion.text?.toString()?.trim() ?: ""

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nombre, apellidos, email y contraseña son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
            return
        }

        val user = UserEntity(
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            password = password,
            rol = "cliente"
        )

        Thread {
            val existente = runBlocking { db.userDao().getByEmail(email) }
            if (existente != null) {
                runOnUiThread {
                    Toast.makeText(this, "Ya existe un usuario con ese email", Toast.LENGTH_LONG).show()
                }
                return@Thread
            }

            runBlocking { db.userDao().insert(user) }

            if (FirebaseManager.disponible) {
                FirebaseManager.registrarUsuario(email, password)?.addOnFailureListener { e ->
                    android.util.Log.w("Registro", "Firebase Auth registro falló (no crítico)", e)
                }
                FirebaseManager.obtenerTokenFCM { token ->
                    if (token != null) {
                        android.util.Log.d("Registro", "FCM token: $token")
                    }
                }
            }

            runOnUiThread {
                binding.txtNombre.text?.clear()
                binding.txtApellidos.text?.clear()
                binding.txtEmail.text?.clear()
                binding.txtPassword.text?.clear()
                binding.txtTelefono.text?.clear()
                binding.txtDireccion.text?.clear()
                binding.txtNombre.requestFocus()

                Toast.makeText(this, "¡Registro completado! Bienvenido, $nombre", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
