package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityTrabajoBinding
import kotlinx.coroutines.runBlocking

class TrabajoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrabajoBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTrabajoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registro trabajador"

        db = AppDatabase.getInstance(this)

        binding.btnAplicar.setOnClickListener {
            guardarYLimpiarDatos()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarYLimpiarDatos() {
        val nombre = binding.etNombre.text?.toString()?.trim() ?: ""
        val apellido = binding.etApellido.text?.toString()?.trim() ?: ""
        val dniNie = binding.etDni.text?.toString()?.trim() ?: ""
        val email = binding.etEmail.text?.toString()?.trim() ?: ""
        val telefono = binding.etTelefono.text?.toString()?.trim() ?: ""

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "El nombre y el apellido son obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        val trabajador = Trabajador(
            nombre = nombre,
            apellido = apellido,
            dni = dniNie,
            email = email,
            telefono = telefono
        )

        Thread {
            val id = runBlocking { db.trabajadorDao().insert(trabajador) }
            runOnUiThread {
                binding.etNombre.text?.clear()
                binding.etApellido.text?.clear()
                binding.etDni.text?.clear()
                binding.etEmail.text?.clear()
                binding.etTelefono.text?.clear()
                binding.etNombre.requestFocus()

                Toast.makeText(
                    this,
                    "Trabajador $nombre registrado con éxito (ID: $id)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
