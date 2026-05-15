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
import com.example.miaplicacion.databinding.ActivityFamiliaBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FamiliaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFamiliaBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: MiembroFamiliarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFamiliaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Círculo familiar"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        adapter = MiembroFamiliarAdapter(
            onDelete = { miembro -> eliminarMiembro(miembro) }
        )
        binding.listaMiembros.layoutManager = LinearLayoutManager(this)
        binding.listaMiembros.adapter = adapter

        binding.btnAgregarMiembro.setOnClickListener {
            startActivity(Intent(this, AgregarMiembroActivity::class.java))
        }

        cargarMiembros()

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarMiembros() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        lifecycleScope.launch {
            db.miembroFamiliarDao().getByMayorId(userId).collectLatest { lista ->
                adapter.submitList(lista)
                binding.txtSinMiembros.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun eliminarMiembro(miembro: MiembroFamiliar) {
        lifecycleScope.launch {
            db.miembroFamiliarDao().delete(miembro)
            FirestoreSyncHelper.eliminarMiembroRemoto(miembro)
            Toast.makeText(this@FamiliaActivity, "Miembro eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
