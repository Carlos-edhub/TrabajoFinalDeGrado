package com.example.miaplicacion

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Trabajador::class, UserEntity::class, Abuelito::class, ServicioHistorial::class,
        Medicacion::class, ContactoEmergencia::class, RegistroSalud::class,
        EventoCalendario::class, MiembroFamiliar::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trabajadorDao(): TrabajadorDao
    abstract fun userDao(): UserDao
    abstract fun abuelitoDao(): AbuelitoDao
    abstract fun servicioHistorialDao(): ServicioHistorialDao
    abstract fun medicacionDao(): MedicacionDao
    abstract fun contactoEmergenciaDao(): ContactoEmergenciaDao
    abstract fun registroSaludDao(): RegistroSaludDao
    abstract fun eventoCalendarioDao(): EventoCalendarioDao
    abstract fun miembroFamiliarDao(): MiembroFamiliarDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "helping_grandpa_db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
