package com.example.miaplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityAgregarSaludBinding
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgregarSaludActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarSaludBinding
    private lateinit var db: AppDatabase

    private val tipos = arrayOf(
        "presion" to "Presión arterial",
        "glucosa" to "Glucosa",
        "peso" to "Peso",
        "animo" to "Estado de ánimo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAgregarSaludBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Añadir registro"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos.map { it.second })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipo.adapter = adapter

        binding.btnGuardar.setOnClickListener { guardarRegistro() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarRegistro() {
        val tipo = tipos[binding.spinnerTipo.selectedItemPosition].first
        val valor = binding.txtValor.text?.toString()?.trim() ?: ""
        val nota = binding.txtNota.text?.toString()?.trim() ?: ""

        if (valor.isEmpty()) {
            Toast.makeText(this, "El valor es obligatorio", Toast.LENGTH_LONG).show()
            return
        }

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val registro = RegistroSalud(
            tipo = tipo,
            valor = valor,
            nota = nota,
            fecha = fecha,
            userId = SessionManager.getUserId()
        )

        Thread {
            runBlocking { db.registroSaludDao().insert(registro) }
            runOnUiThread {
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_LONG).show()
                finish()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
