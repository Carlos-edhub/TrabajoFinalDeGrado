package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicaciones")
data class Medicacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val dosis: String,
    val frecuencia: String,
    val hora: String,
    val activo: Boolean = true,
    val userId: Int = 0
)
