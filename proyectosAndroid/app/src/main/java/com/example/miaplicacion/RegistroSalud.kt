package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros_salud")
data class RegistroSalud(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: String,
    val valor: String,
    val nota: String = "",
    val fecha: String,
    val userId: Int = 0
)
