package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicioHistorialDao {
    @Query("SELECT * FROM servicios_historial ORDER BY id DESC")
    fun getAll(): Flow<List<ServicioHistorial>>

    @Query("SELECT * FROM servicios_historial WHERE userId = :userId ORDER BY id DESC")
    fun getByUserId(userId: Int): Flow<List<ServicioHistorial>>

    @Insert
    suspend fun insert(servicio: ServicioHistorial): Long
}
