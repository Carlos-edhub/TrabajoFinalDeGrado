package com.example.miaplicacion

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.miaplicacion.databinding.ActivityAgregarMedicacionBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class AgregarMedicacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarMedicacionBinding
    private lateinit var db: AppDatabase
    private var editId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAgregarMedicacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        editId = intent.getIntExtra("medicacion_id", 0)
        supportActionBar?.title = if (editId > 0) "Editar medicación" else "Añadir medicación"

        if (editId > 0) {
            cargarMedicacion(editId)
        }

        binding.txtHora.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                binding.txtHora.setText(String.format("%02d:%02d", hour, minute))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        binding.btnGuardar.setOnClickListener { guardarMedicacion() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarMedicacion(id: Int) {
        lifecycleScope.launch {
            val med = db.medicacionDao().getById(id)
            if (med != null) {
                binding.txtNombre.setText(med.nombre)
                binding.txtDosis.setText(med.dosis)
                binding.txtFrecuencia.setText(med.frecuencia)
                binding.txtHora.setText(med.hora)
            }
        }
    }

    private fun guardarMedicacion() {
        val nombre = binding.txtNombre.text?.toString()?.trim() ?: ""
        val dosis = binding.txtDosis.text?.toString()?.trim() ?: ""
        val frecuencia = binding.txtFrecuencia.text?.toString()?.trim() ?: ""
        val hora = binding.txtHora.text?.toString()?.trim() ?: ""

        if (nombre.isEmpty() || dosis.isEmpty() || frecuencia.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch {
            if (editId > 0) {
                val original = db.medicacionDao().getById(editId)
                if (original != null) {
                    val actualizada = original.copy(nombre = nombre, dosis = dosis, frecuencia = frecuencia, hora = hora)
                    db.medicacionDao().update(actualizada)
                    MedicacionWorkScheduler.cancelar(this@AgregarMedicacionActivity, editId)
                    MedicacionWorkScheduler.programar(this@AgregarMedicacionActivity, editId, nombre, dosis, hora)
                    Toast.makeText(this@AgregarMedicacionActivity, "Medicación actualizada", Toast.LENGTH_LONG).show()
                }
            } else {
                val medicacion = Medicacion(
                    nombre = nombre,
                    dosis = dosis,
                    frecuencia = frecuencia,
                    hora = hora,
                    userId = SessionManager.getUserId()
                )
                val id = db.medicacionDao().insert(medicacion)
                if (id > 0) {
                    MedicacionWorkScheduler.programar(this@AgregarMedicacionActivity, id.toInt(), nombre, dosis, hora)
                }
                Toast.makeText(this@AgregarMedicacionActivity, "Medicación guardada con recordatorio", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
