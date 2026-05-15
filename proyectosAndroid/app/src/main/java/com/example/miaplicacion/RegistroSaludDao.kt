package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroSaludDao {
    @Query("SELECT * FROM registros_salud WHERE userId = :userId ORDER BY id DESC")
    fun getByUserId(userId: Int): Flow<List<RegistroSalud>>

    @Query("SELECT * FROM registros_salud WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RegistroSalud?

    @Insert
    suspend fun insert(registro: RegistroSalud): Long

    @Delete
    suspend fun delete(registro: RegistroSalud)
}
