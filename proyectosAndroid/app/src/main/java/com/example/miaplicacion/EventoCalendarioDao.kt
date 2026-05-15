package com.example.miaplicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoCalendarioDao {
    @Query("SELECT * FROM eventos_calendario WHERE userId = :userId ORDER BY fecha DESC, hora DESC")
    fun getByUserId(userId: Int): Flow<List<EventoCalendario>>

    @Query("SELECT * FROM eventos_calendario WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): EventoCalendario?

    @Insert
    suspend fun insert(evento: EventoCalendario): Long

    @Update
    suspend fun update(evento: EventoCalendario)

    @Delete
    suspend fun delete(evento: EventoCalendario)

    @Query("DELETE FROM eventos_calendario WHERE userId = :userId AND fecha < :fechaLimite")
    suspend fun deleteOlderThan(userId: Int, fechaLimite: String)
}
