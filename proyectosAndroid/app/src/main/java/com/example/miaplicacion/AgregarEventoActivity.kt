package com.example.miaplicacion

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityAgregarEventoBinding
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class AgregarEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarEventoBinding
    private lateinit var db: AppDatabase

    private val tipos = arrayOf("personal", "médica", "familiar", "social")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAgregarEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Añadir evento"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos.map { it.replaceFirstChar { it.uppercase() } })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipo.adapter = adapter

        binding.txtFecha.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                binding.txtFecha.setText(String.format("%02d/%02d/%04d", d, m + 1, y))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.txtHora.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                binding.txtHora.setText(String.format("%02d:%02d", hour, minute))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        binding.btnGuardar.setOnClickListener { guardarEvento() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarEvento() {
        val titulo = binding.txtTitulo.text?.toString()?.trim() ?: ""
        val descripcion = binding.txtDescripcion.text?.toString()?.trim() ?: ""
        val fecha = binding.txtFecha.text?.toString()?.trim() ?: ""
        val hora = binding.txtHora.text?.toString()?.trim() ?: ""
        val tipo = tipos[binding.spinnerTipo.selectedItemPosition]

        if (titulo.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Título y fecha son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val evento = EventoCalendario(
            titulo = titulo,
            descripcion = descripcion,
            fecha = fecha,
            hora = hora,
            tipo = tipo,
            userId = SessionManager.getUserId()
        )

        Thread {
            val id = runBlocking { db.eventoCalendarioDao().insert(evento) }
            if (id > 0) {
                FirestoreSyncHelper.syncEvento(this, evento.copy(id = id.toInt()))
            }
            runOnUiThread {
                Toast.makeText(this, "Evento guardado", Toast.LENGTH_LONG).show()
                finish()
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
