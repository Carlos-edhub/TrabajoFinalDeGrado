package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrabajadorDao {
    @Query("SELECT * FROM trabajadores ORDER BY idTrabajador DESC")
    fun getAll(): Flow<List<Trabajador>>

    @Query("SELECT * FROM trabajadores ORDER BY idTrabajador DESC")
    fun getAllList(): List<Trabajador>

    @Query("SELECT * FROM trabajadores WHERE idTrabajador = :id")
    suspend fun getById(id: Int): Trabajador?

    @Insert
    suspend fun insert(trabajador: Trabajador): Long

    @Update
    suspend fun update(trabajador: Trabajador)

    @Delete
    suspend fun delete(trabajador: Trabajador)

    @Query("DELETE FROM trabajadores")
    suspend fun deleteAll()
}
