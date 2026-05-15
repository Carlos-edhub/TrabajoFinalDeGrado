package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityServicioBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

class ServicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServicioBinding
    private lateinit var db: AppDatabase
    private val precioBase = 3.99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityServicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Servicio"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        binding.btnSolicitarServicio.setOnClickListener {
            solicitarServicio()
        }

        binding.fechaMedicas.setOnClickListener {
            showDatePicker { date -> binding.fechaMedicas.setText(date) }
        }
        binding.fechaCompras.setOnClickListener {
            showDatePicker { date -> binding.fechaCompras.setText(date) }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun solicitarServicio() {
        val horasMedicasStr = binding.horasMedicas.text?.toString() ?: ""
        val horasComprasStr = binding.horasCompras.text?.toString() ?: ""
        val fechaMedicas = binding.fechaMedicas.text?.toString()?.trim() ?: ""
        val fechaCompras = binding.fechaCompras.text?.toString()?.trim() ?: ""

        val horasMedicas = horasMedicasStr.toIntOrNull() ?: 0
        val horasCompras = horasComprasStr.toIntOrNull() ?: 0
        val totalHoras = horasMedicas + horasCompras

        if (totalHoras <= 0) {
            Toast.makeText(this, "Indica al menos 1 hora para algún servicio", Toast.LENGTH_LONG).show()
            return
        }

        val total = totalHoras * precioBase
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val servicio = ServicioHistorial(
            horasMedicas = horasMedicas,
            fechaMedicas = fechaMedicas,
            horasCompras = horasCompras,
            fechaCompras = fechaCompras,
            totalHoras = totalHoras,
            totalPrecio = total,
            fechaSolicitud = fechaActual,
            userId = SessionManager.getUserId()
        )

        Thread {
            kotlinx.coroutines.runBlocking { db.servicioHistorialDao().insert(servicio) }
            runOnUiThread {
                binding.horasMedicas.text?.clear()
                binding.fechaMedicas.text?.clear()
                binding.horasCompras.text?.clear()
                binding.fechaCompras.text?.clear()
                binding.editTextNumber.text?.clear()

                val mensaje = """
                    Servicio solicitado:
                    - Citas médicas: $horasMedicas h
                    - Compras: $horasCompras h
                    Total: $totalHoras h → %.2f €
                """.trimIndent().format(total)

                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
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
