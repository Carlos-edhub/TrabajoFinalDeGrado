package com.example.miaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miaplicacion.databinding.ItemMedicacionBinding

class MedicacionAdapter(
    private val onEdit: (Medicacion) -> Unit,
    private val onDelete: (Medicacion) -> Unit
) : ListAdapter<Medicacion, MedicacionAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMedicacionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMedicacionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(medicacion: Medicacion) {
            binding.txtNombreMed.text = medicacion.nombre
            binding.txtInfoMed.text = "${medicacion.dosis} - ${medicacion.frecuencia} - ${medicacion.hora}"
            binding.root.setOnClickListener { onEdit(medicacion) }
            binding.btnEliminarMed.setOnClickListener { onDelete(medicacion) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Medicacion>() {
        override fun areItemsTheSame(oldItem: Medicacion, newItem: Medicacion) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Medicacion, newItem: Medicacion) = oldItem == newItem
    }
}
