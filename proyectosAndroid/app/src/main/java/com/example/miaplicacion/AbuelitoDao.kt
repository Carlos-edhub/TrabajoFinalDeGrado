package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AbuelitoDao {
    @Query("SELECT * FROM abuelitos ORDER BY id DESC")
    fun getAll(): Flow<List<Abuelito>>

    @Query("SELECT * FROM abuelitos WHERE userId = :userId ORDER BY id DESC")
    fun getByUserId(userId: Int): Flow<List<Abuelito>>

    @Query("SELECT * FROM abuelitos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Abuelito?

    @Insert
    suspend fun insert(abuelito: Abuelito): Long

    @Update
    suspend fun update(abuelito: Abuelito)

    @Delete
    suspend fun delete(abuelito: Abuelito)
}
