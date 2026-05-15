package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miaplicacion.databinding.ActivityMedicacionBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MedicacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicacionBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: MedicacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMedicacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Medicación"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        adapter = MedicacionAdapter(
            onEdit = { medicacion -> editarMedicacion(medicacion) },
            onDelete = { medicacion -> eliminarMedicacion(medicacion) }
        )
        binding.listaMedicacion.layoutManager = LinearLayoutManager(this)
        binding.listaMedicacion.adapter = adapter

        binding.btnAgregarMedicacion.setOnClickListener {
            startActivity(Intent(this, AgregarMedicacionActivity::class.java))
        }

        cargarMedicaciones()

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarMedicaciones() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        lifecycleScope.launch {
            db.medicacionDao().getByUserId(userId).collectLatest { lista ->
                adapter.submitList(lista)
                binding.txtSinMedicacion.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun editarMedicacion(medicacion: Medicacion) {
        val intent = Intent(this, AgregarMedicacionActivity::class.java)
        intent.putExtra("medicacion_id", medicacion.id)
        startActivity(intent)
    }

    private fun eliminarMedicacion(medicacion: Medicacion) {
        lifecycleScope.launch {
            db.medicacionDao().delete(medicacion)
            MedicacionWorkScheduler.cancelar(this@MedicacionActivity, medicacion.id)
            Toast.makeText(this@MedicacionActivity, "Medicación eliminada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
