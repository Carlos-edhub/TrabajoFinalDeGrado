package com.example.miaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miaplicacion.databinding.ItemContactoEmergenciaBinding

class ContactoEmergenciaAdapter(
    private val onDelete: (ContactoEmergencia) -> Unit
) : ListAdapter<ContactoEmergencia, ContactoEmergenciaAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactoEmergenciaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemContactoEmergenciaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contacto: ContactoEmergencia) {
            binding.txtNombreContacto.text = contacto.nombre
            binding.txtTelefonoContacto.text = contacto.telefono
            binding.btnEliminarContacto.setOnClickListener { onDelete(contacto) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ContactoEmergencia>() {
        override fun areItemsTheSame(oldItem: ContactoEmergencia, newItem: ContactoEmergencia) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ContactoEmergencia, newItem: ContactoEmergencia) = oldItem == newItem
    }
}
