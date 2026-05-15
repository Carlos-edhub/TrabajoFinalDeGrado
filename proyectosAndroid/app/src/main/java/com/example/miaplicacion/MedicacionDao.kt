package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicacionDao {
    @Query("SELECT * FROM medicaciones WHERE userId = :userId ORDER BY id DESC")
    fun getByUserId(userId: Int): Flow<List<Medicacion>>

    @Query("SELECT * FROM medicaciones WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Medicacion?

    @Insert
    suspend fun insert(medicacion: Medicacion): Long

    @Update
    suspend fun update(medicacion: Medicacion)

    @Delete
    suspend fun delete(medicacion: Medicacion)
}
