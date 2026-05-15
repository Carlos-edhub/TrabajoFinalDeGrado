package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val fechaNacimiento: String = "",
    val rol: String = "usuario" // usuario, cuidador, profesional
)
