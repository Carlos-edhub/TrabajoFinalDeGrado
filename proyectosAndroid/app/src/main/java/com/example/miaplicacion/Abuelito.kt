package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abuelitos")
data class Abuelito(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellidos: String,
    val edad: String,
    val direccion: String = "",
    val telefonoContacto: String = "",
    val necesidades: String = "",
    val userId: Int = 0
)
