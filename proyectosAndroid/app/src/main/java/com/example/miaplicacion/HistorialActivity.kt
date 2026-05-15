package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miaplicacion.databinding.ActivityHistorialBinding
import kotlinx.coroutines.launch

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Historial"

        SessionManager.init(this)
        db = AppDatabase.getInstance(this)

        adapter = HistorialAdapter(emptyList())
        binding.listaHistorial.layoutManager = LinearLayoutManager(this)
        binding.listaHistorial.adapter = adapter

        val userId = SessionManager.getUserId()
        if (userId > 0) {
            lifecycleScope.launch {
                db.servicioHistorialDao().getByUserId(userId).collect { lista ->
                    if (lista.isEmpty()) {
                        binding.mensajeVacio.visibility = View.VISIBLE
                        binding.listaHistorial.visibility = View.GONE
                    } else {
                        binding.mensajeVacio.visibility = View.GONE
                        binding.listaHistorial.visibility = View.VISIBLE
                        adapter.actualizar(lista)
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

class HistorialAdapter(private var items: List<ServicioHistorial>) :
    RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitulo: TextView = view.findViewById(R.id.txtTitulo)
        val txtDetalle: TextView = view.findViewById(R.id.txtDetalle)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitulo.text = "${item.fechaSolicitud} — ${item.totalHoras}h"
        holder.txtDetalle.text = "Médicas: ${item.horasMedicas}h · Compras: ${item.horasCompras}h"
        holder.txtPrecio.text = "%.2f €".format(item.totalPrecio)
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<ServicioHistorial>) {
        items = nuevos
        notifyDataSetChanged()
    }
}
