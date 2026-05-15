package com.example.miaplicacion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityAgregarContactoBinding
import kotlinx.coroutines.runBlocking

class AgregarContactoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarContactoBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAgregarContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Añadir contacto"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        binding.btnGuardar.setOnClickListener { guardarContacto() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarContacto() {
        val nombre = binding.txtNombre.text?.toString()?.trim() ?: ""
        val telefono = binding.txtTelefono.text?.toString()?.trim() ?: ""
        val email = binding.txtEmail.text?.toString()?.trim() ?: ""

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val contacto = ContactoEmergencia(
            nombre = nombre,
            telefono = telefono,
            email = email,
            userId = SessionManager.getUserId()
        )

        Thread {
            runBlocking { db.contactoEmergenciaDao().insert(contacto) }
            runOnUiThread {
                Toast.makeText(this, "Contacto guardado", Toast.LENGTH_LONG).show()
                finish()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
