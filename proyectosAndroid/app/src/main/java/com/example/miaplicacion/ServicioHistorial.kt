package com.example.miaplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicios_historial")
data class ServicioHistorial(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val horasMedicas: Int = 0,
    val fechaMedicas: String = "",
    val horasCompras: Int = 0,
    val fechaCompras: String = "",
    val totalHoras: Int = 0,
    val totalPrecio: Double = 0.0,
    val fechaSolicitud: String = "",
    val userId: Int = 0
)
