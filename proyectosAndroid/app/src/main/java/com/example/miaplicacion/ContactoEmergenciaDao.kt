package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactoEmergenciaDao {
    @Query("SELECT * FROM contactos_emergencia WHERE userId = :userId ORDER BY id DESC")
    fun getByUserId(userId: Int): Flow<List<ContactoEmergencia>>

    @Query("SELECT * FROM contactos_emergencia WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ContactoEmergencia?

    @Insert
    suspend fun insert(contacto: ContactoEmergencia): Long

    @Update
    suspend fun update(contacto: ContactoEmergencia)

    @Delete
    suspend fun delete(contacto: ContactoEmergencia)

    @Query("SELECT * FROM contactos_emergencia WHERE userId = :userId")
    suspend fun getAllByUserId(userId: Int): List<ContactoEmergencia>
}
