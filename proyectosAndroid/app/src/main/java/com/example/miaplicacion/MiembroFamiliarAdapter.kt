package com.example.miaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miaplicacion.databinding.ItemMiembroFamiliaBinding

class MiembroFamiliarAdapter(
    private val onDelete: (MiembroFamiliar) -> Unit
) : ListAdapter<MiembroFamiliar, MiembroFamiliarAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMiembroFamiliaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMiembroFamiliaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(miembro: MiembroFamiliar) {
            binding.txtNombreMiembro.text = miembro.nombre
            binding.txtEmailMiembro.text = miembro.email
            binding.txtRolMiembro.text = when (miembro.rol) {
                "admin" -> "Administrador"
                "caregiver" -> "Cuidador"
                "viewer" -> "Espectador"
                else -> miembro.rol
            }
            binding.btnEliminarMiembro.setOnClickListener { onDelete(miembro) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MiembroFamiliar>() {
        override fun areItemsTheSame(oldItem: MiembroFamiliar, newItem: MiembroFamiliar) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MiembroFamiliar, newItem: MiembroFamiliar) = oldItem == newItem
    }
}
