package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityRegistrarAbuelitoBinding

class RegistrarAbuelitoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarAbuelitoBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegistrarAbuelitoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registrar Mayor"

        db = AppDatabase.getInstance(this)

        binding.btnGuardar.setOnClickListener {
            guardarAbuelito()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarAbuelito() {
        val nombre = binding.txtNombre.text?.toString()?.trim() ?: ""
        val apellidos = binding.txtApellidos.text?.toString()?.trim() ?: ""
        val edad = binding.txtEdad.text?.toString()?.trim() ?: ""
        val direccion = binding.txtDireccion.text?.toString()?.trim() ?: ""
        val telefono = binding.txtTelefono.text?.toString()?.trim() ?: ""
        val necesidades = binding.txtNecesidades.text?.toString()?.trim() ?: ""

        if (nombre.isEmpty() || apellidos.isEmpty() || edad.isEmpty()) {
            Toast.makeText(this, "Nombre, apellidos y edad son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val abuelito = Abuelito(
            nombre = nombre,
            apellidos = apellidos,
            edad = edad,
            direccion = direccion,
            telefonoContacto = telefono,
            necesidades = necesidades,
            userId = SessionManager.getUserId()
        )

        Thread {
            kotlinx.coroutines.runBlocking { db.abuelitoDao().insert(abuelito) }
            runOnUiThread {
                binding.txtNombre.text?.clear()
                binding.txtApellidos.text?.clear()
                binding.txtEdad.text?.clear()
                binding.txtDireccion.text?.clear()
                binding.txtTelefono.text?.clear()
                binding.txtNecesidades.text?.clear()
                binding.txtNombre.requestFocus()

                Toast.makeText(this, "Mayor registrado correctamente", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
