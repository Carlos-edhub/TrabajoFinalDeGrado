package com.example.miaplicacion

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miaplicacion.databinding.ActivitySosBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SosActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySosBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: ContactoEmergenciaAdapter
    private var pendienteDeEnvio = false

    private val permisosSosLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        val todosConcedidos = PermissionHelper.SOS_PERMISSIONS.all {
            resultados.getOrDefault(it, false)
        }
        if (todosConcedidos) {
            ejecutarSos()
        } else {
            Toast.makeText(
                this,
                "Se necesitan permisos de SMS, llamada y ubicación para el SOS",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Emergencia"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        adapter = ContactoEmergenciaAdapter(
            onDelete = { contacto -> eliminarContacto(contacto) }
        )
        binding.listaContactos.layoutManager = LinearLayoutManager(this)
        binding.listaContactos.adapter = adapter

        binding.btnActivarSos.setOnClickListener { activarSos() }

        binding.btnAgregarContacto.setOnClickListener {
            startActivity(Intent(this, AgregarContactoActivity::class.java))
        }

        cargarContactos()

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarContactos() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        lifecycleScope.launch {
            db.contactoEmergenciaDao().getByUserId(userId).collectLatest { lista ->
                adapter.submitList(lista)
                binding.txtSinContactos.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun activarSos() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) {
            Toast.makeText(this, "Inicia sesión para usar SOS", Toast.LENGTH_LONG).show()
            return
        }

        if (!PermissionHelper.hasSosPermissions(this)) {
            Toast.makeText(this, "Concediendo permisos necesarios...", Toast.LENGTH_SHORT).show()
            permisosSosLauncher.launch(PermissionHelper.SOS_PERMISSIONS)
            return
        }

        ejecutarSos()
    }

    private fun ejecutarSos() {
        val userId = SessionManager.getUserId()
        lifecycleScope.launch {
            val contactos = db.contactoEmergenciaDao().getAllByUserId(userId)
            if (contactos.isEmpty()) {
                Toast.makeText(this@SosActivity, "Añade al menos un contacto de emergencia", Toast.LENGTH_LONG).show()
                return@launch
            }
            mostrarConfirmacionSos(contactos)
        }
    }

    private fun mostrarConfirmacionSos(contactos: List<ContactoEmergencia>) {
        val nombres = contactos.joinToString("\n") { "• ${it.nombre} (${it.telefono})" }
        android.app.AlertDialog.Builder(this)
            .setTitle("¿Enviar SOS?")
            .setMessage(
                "Se enviará un SMS y WhatsApp con tu ubicación, y se llamará a:\n\n$nombres\n\n" +
                "¿Estás seguro de que quieres activar la emergencia?"
            )
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("SÍ, ACTIVAR SOS") { _, _ ->
                SosHelper.lanzarSos(this, contactos)
            }
            .setNegativeButton("Cancelar", null)
            .setCancelable(true)
            .show()
    }

    private fun eliminarContacto(contacto: ContactoEmergencia) {
        lifecycleScope.launch {
            db.contactoEmergenciaDao().delete(contacto)
            Toast.makeText(this@SosActivity, "Contacto eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
