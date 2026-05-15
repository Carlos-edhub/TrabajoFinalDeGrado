package com.example.miaplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityAgregarMiembroBinding
import kotlinx.coroutines.runBlocking

class AgregarMiembroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarMiembroBinding
    private lateinit var db: AppDatabase

    private val roles = arrayOf(
        "viewer" to "Espectador (puede ver)",
        "caregiver" to "Cuidador (puede ver y cuidar)",
        "admin" to "Administrador (control total)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAgregarMiembroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Invitar miembro"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles.map { it.second })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRol.adapter = adapter

        binding.btnGuardar.setOnClickListener { guardarMiembro() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarMiembro() {
        val nombre = binding.txtNombre.text?.toString()?.trim() ?: ""
        val email = binding.txtEmail.text?.toString()?.trim() ?: ""
        val telefono = binding.txtTelefono.text?.toString()?.trim() ?: ""
        val rol = roles[binding.spinnerRol.selectedItemPosition].first

        if (nombre.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nombre y email son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val miembro = MiembroFamiliar(
            nombre = nombre,
            email = email,
            telefono = telefono,
            rol = rol,
            mayorId = SessionManager.getUserId(),
            userId = SessionManager.getUserId()
        )

        Thread {
            val id = runBlocking { db.miembroFamiliarDao().insert(miembro) }
            if (id > 0) {
                FirestoreSyncHelper.syncMiembro(this, miembro.copy(id = id.toInt()))
            }
            runOnUiThread {
                Toast.makeText(this, "Miembro invitado correctamente", Toast.LENGTH_LONG).show()
                finish()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
