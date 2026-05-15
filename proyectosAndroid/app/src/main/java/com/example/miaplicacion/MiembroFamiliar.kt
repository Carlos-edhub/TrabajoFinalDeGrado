package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miembros_familia")
data class MiembroFamiliar(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val telefono: String = "",
    val rol: String = "viewer",
    val mayorId: Int = 0,
    val userId: Int = 0
)
