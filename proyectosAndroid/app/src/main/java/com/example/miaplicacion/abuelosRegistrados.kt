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
import com.example.miaplicacion.databinding.ActivityAbuelosRegistradosBinding
import kotlinx.coroutines.launch

class abuelosRegistrados : AppCompatActivity() {

    private lateinit var binding: ActivityAbuelosRegistradosBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: AbuelitoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAbuelosRegistradosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Abuelitos"

        db = AppDatabase.getInstance(this)

        adapter = AbuelitoAdapter(emptyList())
        binding.listaAbuelitos.layoutManager = LinearLayoutManager(this)
        binding.listaAbuelitos.adapter = adapter

        lifecycleScope.launch {
            db.abuelitoDao().getAll().collect { lista ->
                if (lista.isEmpty()) {
                    binding.mensajeVacio.visibility = View.VISIBLE
                    binding.listaAbuelitos.visibility = View.GONE
                } else {
                    binding.mensajeVacio.visibility = View.GONE
                    binding.listaAbuelitos.visibility = View.VISIBLE
                    adapter.actualizar(lista)
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

class AbuelitoAdapter(private var items: List<Abuelito>) :
    RecyclerView.Adapter<AbuelitoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtDetalle: TextView = view.findViewById(R.id.txtDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_abuelito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtNombre.text = "${item.nombre} ${item.apellidos}"
        holder.txtDetalle.text = "Edad: ${item.edad} | Contacto: ${item.telefonoContacto}"
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<Abuelito>) {
        items = nuevos
        notifyDataSetChanged()
    }
}
