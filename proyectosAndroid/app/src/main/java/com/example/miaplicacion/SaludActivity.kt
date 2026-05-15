package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miaplicacion.databinding.ActivitySaludBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SaludActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaludBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: RegistroSaludAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySaludBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Historial de salud"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        adapter = RegistroSaludAdapter(
            onDelete = { registro -> eliminarRegistro(registro) }
        )
        binding.listaSalud.layoutManager = LinearLayoutManager(this)
        binding.listaSalud.adapter = adapter

        binding.btnAgregarRegistro.setOnClickListener {
            startActivity(Intent(this, AgregarSaludActivity::class.java))
        }

        binding.btnExportarCSV.setOnClickListener { exportarCSV() }

        binding.btnVerGraficas.setOnClickListener {
            startActivity(Intent(this, SaludGraficaActivity::class.java))
        }

        cargarRegistros()

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarRegistros() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        lifecycleScope.launch {
            db.registroSaludDao().getByUserId(userId).collectLatest { lista ->
                listaRegistros = lista
                adapter.submitList(lista)
                binding.txtSinRegistros.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun eliminarRegistro(registro: RegistroSalud) {
        lifecycleScope.launch {
            db.registroSaludDao().delete(registro)
        }
    }

    private var listaRegistros = listOf<RegistroSalud>()

    private fun exportarCSV() {
        CsvExporter.exportarRegistrosSalud(this, listaRegistros)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
