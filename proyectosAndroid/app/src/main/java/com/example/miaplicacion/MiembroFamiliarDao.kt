package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MiembroFamiliarDao {
    @Query("SELECT * FROM miembros_familia WHERE mayorId = :mayorId ORDER BY id DESC")
    fun getByMayorId(mayorId: Int): Flow<List<MiembroFamiliar>>

    @Query("SELECT * FROM miembros_familia WHERE mayorId = :mayorId")
    suspend fun getAllByMayorId(mayorId: Int): List<MiembroFamiliar>

    @Query("SELECT * FROM miembros_familia WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): MiembroFamiliar?

    @Insert
    suspend fun insert(miembro: MiembroFamiliar): Long

    @Update
    suspend fun update(miembro: MiembroFamiliar)

    @Delete
    suspend fun delete(miembro: MiembroFamiliar)
}
