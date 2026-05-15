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
import com.example.miaplicacion.databinding.ActivityCalendarioBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: EventoCalendarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Calendario"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        adapter = EventoCalendarioAdapter(
            onDelete = { evento -> eliminarEvento(evento) }
        )
        binding.listaEventos.layoutManager = LinearLayoutManager(this)
        binding.listaEventos.adapter = adapter

        binding.btnAgregarEvento.setOnClickListener {
            startActivity(Intent(this, AgregarEventoActivity::class.java))
        }

        cargarEventos()

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarEventos() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        lifecycleScope.launch {
            db.eventoCalendarioDao().getByUserId(userId).collectLatest { lista ->
                adapter.submitList(lista)
                binding.txtSinEventos.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun eliminarEvento(evento: EventoCalendario) {
        lifecycleScope.launch {
            db.eventoCalendarioDao().delete(evento)
            FirestoreSyncHelper.eliminarEventoRemoto(evento)
            Toast.makeText(this@CalendarioActivity, "Evento eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
