package com.example.miaplicacion

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.miaplicacion.databinding.ActivitySaludGraficaBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SaludGraficaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaludGraficaBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySaludGraficaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Gráficas de salud"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        cargarDatos()

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarDatos() {
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        lifecycleScope.launch {
            val todos = db.registroSaludDao().getByUserId(userId).first()

            val presion = todos.filter { it.tipo == "presion" }.mapNotNull { r ->
                val v = r.valor.split("/").firstOrNull()?.trim()?.toFloatOrNull()
                if (v != null) r.fecha to v else null
            }.takeLast(20)

            val glucosa = todos.filter { it.tipo == "glucosa" }.mapNotNull { r ->
                r.valor.toFloatOrNull()?.let { r.fecha to it }
            }.takeLast(20)

            val peso = todos.filter { it.tipo == "peso" }.mapNotNull { r ->
                r.valor.filter { it.isDigit() || it == '.' }.toFloatOrNull()?.let { r.fecha to it }
            }.takeLast(20)

            binding.chartPresion.puntos = presion
            binding.chartPresion.colorLinea = Color.parseColor("#D92D20")
            binding.txtTituloPresion.visibility = if (presion.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            binding.chartPresion.visibility = if (presion.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE

            binding.chartGlucosa.puntos = glucosa
            binding.chartGlucosa.colorLinea = Color.parseColor("#1E8E3E")
            binding.txtTituloGlucosa.visibility = if (glucosa.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            binding.chartGlucosa.visibility = if (glucosa.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE

            binding.chartPeso.puntos = peso
            binding.chartPeso.colorLinea = Color.parseColor("#0055CC")
            binding.txtTituloPeso.visibility = if (peso.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            binding.chartPeso.visibility = if (peso.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE

            if (presion.isEmpty() && glucosa.isEmpty() && peso.isEmpty()) {
                binding.txtSinDatos.visibility = android.view.View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
