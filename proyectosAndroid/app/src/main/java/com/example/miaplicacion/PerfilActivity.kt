package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityPerfilBinding
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var db: AppDatabase

    private val roles = arrayOf("Usuario (Mayor)", "Cuidador", "Profesional")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mi Perfil"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRol.adapter = adapter

        cargarDatos()

        binding.btnGuardar.setOnClickListener {
            guardarCambios()
        }

        binding.txtFechaNacimiento.setOnClickListener {
            showDatePicker { date -> binding.txtFechaNacimiento.setText(date) }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarDatos() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        Thread {
            val user = runBlocking { db.userDao().getById(userId) }
            runOnUiThread {
                if (user != null) {
                    binding.txtNombre.setText(user.nombre)
                    binding.txtApellidos.setText(user.apellidos)
                    binding.txtEmail.setText(user.email)
                    binding.txtFechaNacimiento.setText(user.fechaNacimiento)

                    val pos = roles.indexOfFirst { it == user.rol }
                    if (pos >= 0) binding.spinnerRol.setSelection(pos)
                }
            }
        }.start()
    }

    private fun guardarCambios() {
        val nombre = binding.txtNombre.text?.toString()?.trim() ?: ""
        val apellidos = binding.txtApellidos.text?.toString()?.trim() ?: ""
        val email = binding.txtEmail.text?.toString()?.trim() ?: ""
        val fechaNac = binding.txtFechaNacimiento.text?.toString()?.trim() ?: ""
        val rol = roles[binding.spinnerRol.selectedItemPosition]

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nombre, apellidos y email son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        Thread {
            val user = runBlocking { db.userDao().getById(userId) }
            if (user != null) {
                val updated = user.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    email = email,
                    fechaNacimiento = fechaNac,
                    rol = rol
                )
                runBlocking { db.userDao().update(updated) }
                SessionManager.saveSession(updated)
                runOnUiThread {
                    Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val picker = android.app.DatePickerDialog(this, { _, y, m, d ->
            onDateSelected(String.format("%02d/%02d/%04d", d, m + 1, y))
        }, year, month, day)
        picker.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
