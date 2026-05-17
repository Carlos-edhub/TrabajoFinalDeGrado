package com.example.miaplicacion

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val horas = s?.toString()?.toIntOrNull() ?: 0
                val total = horas * precioBase
                binding.txtTotalCalculado.text = "Total: %.2f €".format(total)
            }
        })

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
        val horasEditadas = binding.editTextNumber.text?.toString()?.toIntOrNull() ?: 0

        val horasMedicas = horasMedicasStr.toIntOrNull() ?: 0
        val horasCompras = horasComprasStr.toIntOrNull() ?: 0
        val totalHoras = if (horasEditadas > 0) horasEditadas else horasMedicas + horasCompras

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

                mostrarFactura(servicio)
            }
        }.start()
    }

    private fun mostrarFactura(servicio: ServicioHistorial) {
        val factura = """
            ═══════════════════════════════
                     FACTURA SERVICIO
            ═══════════════════════════════

            Fecha: ${servicio.fechaSolicitud}

            ───────────────────────────────
            Citas médicas:
              Horas: ${servicio.horasMedicas}h
              Fecha: ${servicio.fechaMedicas}

            Compras supermercado:
              Horas: ${servicio.horasCompras}h
              Fecha: ${servicio.fechaCompras}
            ───────────────────────────────

            Precio por hora: %.2f €
            Total horas: %d h

            TOTAL A PAGAR: %.2f €
            ═══════════════════════════════
        """.trimIndent().format(precioBase, servicio.totalHoras, servicio.totalPrecio)

        AlertDialog.Builder(this)
            .setTitle("Factura")
            .setMessage(factura)
            .setPositiveButton("Aceptar", null)
            .show()
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
