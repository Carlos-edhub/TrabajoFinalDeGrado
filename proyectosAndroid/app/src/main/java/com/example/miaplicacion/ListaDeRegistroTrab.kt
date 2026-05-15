package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityListaDeRegistroTrabBinding

class ListaDeRegistroTrab : AppCompatActivity() {

    private lateinit var binding: ActivityListaDeRegistroTrabBinding
    private lateinit var db: AppDatabase
    private lateinit var trabajadorAdapter: ArrayAdapter<Trabajador>
    private var listaTrabajadores = mutableListOf<Trabajador>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListaDeRegistroTrabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        binding.btnVolverMod.setOnClickListener {
            startActivity(Intent(this, BaseDatosHelpingAgP::class.java))
        }

        trabajadorAdapter = ArrayAdapter<Trabajador>(
            this, android.R.layout.simple_spinner_item,
            mutableListOf()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerTrabajadores.adapter = trabajadorAdapter

        binding.spinnerTrabajadores.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if ((parent?.adapter?.count ?: 0) > 0) {
                        val seleccionado = parent?.getItemAtPosition(position) as Trabajador
                        val detalles = """
                            ID: ${seleccionado.idTrabajador}
                            Nombre: ${seleccionado.nombre} ${seleccionado.apellido}
                            DNI: ${seleccionado.dni}
                            Email: ${seleccionado.email}
                            Teléfono: ${seleccionado.telefono}
                        """.trimIndent()
                        binding.textAreaDetalles.text = detalles
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.textAreaDetalles.text = "Selecciona un trabajador de la lista."
                }
            }

        binding.busquedaTrabajador.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filtrar(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrar(newText.orEmpty())
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        cargarTrabajadores()
    }

    private fun cargarTrabajadores() {
        Thread {
            val lista = db.trabajadorDao().getAllList()
            runOnUiThread {
                listaTrabajadores = lista.toMutableList()
                trabajadorAdapter.clear()
                trabajadorAdapter.addAll(listaTrabajadores)
                trabajadorAdapter.notifyDataSetChanged()
                if (listaTrabajadores.isEmpty()) {
                    binding.textAreaDetalles.text = "No hay trabajadores registrados."
                }
            }
        }.start()
    }

    private fun filtrar(query: String) {
        if (query.isBlank()) {
            trabajadorAdapter.clear()
            trabajadorAdapter.addAll(listaTrabajadores)
            trabajadorAdapter.notifyDataSetChanged()
            return
        }

        val filtrados = listaTrabajadores.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.apellido.contains(query, ignoreCase = true) ||
            it.idTrabajador.toString() == query
        }

        trabajadorAdapter.clear()
        trabajadorAdapter.addAll(filtrados)
        trabajadorAdapter.notifyDataSetChanged()

        if (filtrados.isNotEmpty()) {
            val t = filtrados.first()
            binding.textAreaDetalles.text = """
                ID: ${t.idTrabajador}
                Nombre: ${t.nombre} ${t.apellido}
                DNI: ${t.dni}
                Email: ${t.email}
                Teléfono: ${t.telefono}
            """.trimIndent()
        } else {
            binding.textAreaDetalles.text = "No se encontró \"$query\"."
        }
    }
}
