package com.example.miaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miaplicacion.databinding.ItemEventoBinding

class EventoCalendarioAdapter(
    private val onDelete: (EventoCalendario) -> Unit
) : ListAdapter<EventoCalendario, EventoCalendarioAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemEventoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(evento: EventoCalendario) {
            binding.txtTituloEvento.text = evento.titulo
            val fechaStr = buildString {
                append(evento.fecha)
                if (evento.hora.isNotEmpty()) append(" - ${evento.hora}")
            }
            binding.txtFechaEvento.text = fechaStr
            binding.txtDescEvento.text = evento.descripcion
            binding.txtDescEvento.visibility = if (evento.descripcion.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            binding.btnEliminarEvento.setOnClickListener { onDelete(evento) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EventoCalendario>() {
        override fun areItemsTheSame(oldItem: EventoCalendario, newItem: EventoCalendario) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: EventoCalendario, newItem: EventoCalendario) = oldItem == newItem
    }
}
