package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos_calendario")
data class EventoCalendario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String = "",
    val fecha: String,
    val hora: String = "",
    val tipo: String = "personal",
    val userId: Int = 0
)
