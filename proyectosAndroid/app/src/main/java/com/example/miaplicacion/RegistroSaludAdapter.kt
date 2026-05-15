package com.example.miaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miaplicacion.databinding.ItemRegistroSaludBinding

class RegistroSaludAdapter(
    private val onDelete: (RegistroSalud) -> Unit
) : ListAdapter<RegistroSalud, RegistroSaludAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRegistroSaludBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemRegistroSaludBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(registro: RegistroSalud) {
            binding.txtTipoSalud.text = when (registro.tipo) {
                "presion" -> "Presión arterial"
                "glucosa" -> "Glucosa"
                "peso" -> "Peso"
                "animo" -> "Estado de ánimo"
                else -> registro.tipo
            }
            binding.txtValorSalud.text = registro.valor
            binding.txtFechaSalud.text = registro.fecha
            binding.txtNotaSalud.text = registro.nota
            binding.txtNotaSalud.visibility = if (registro.nota.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RegistroSalud>() {
        override fun areItemsTheSame(oldItem: RegistroSalud, newItem: RegistroSalud) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RegistroSalud, newItem: RegistroSalud) = oldItem == newItem
    }
}
