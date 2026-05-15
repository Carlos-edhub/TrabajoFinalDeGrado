package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "trabajadores")
data class Trabajador(
    @PrimaryKey(autoGenerate = true)
    val idTrabajador: Int = 0,
    val nombre: String,
    val apellido: String,
    val dni: String = "",
    val email: String = "",
    val telefono: String = ""
) : Serializable {

    override fun toString(): String {
        return "$nombre $apellido"
    }
}
