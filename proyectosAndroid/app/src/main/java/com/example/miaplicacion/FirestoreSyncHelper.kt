package com.example.miaplicacion

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.runBlocking

object FirestoreSyncHelper {
    private const val TAG = "FirestoreSync"
    private const val COL_EVENTOS = "eventos_calendario"
    private const val COL_FAMILIA = "miembros_familia"

    private var db: com.google.firebase.firestore.FirebaseFirestore? = null
    private var escuchaEventos: ListenerRegistration? = null
    private var escuchaFamilia: ListenerRegistration? = null

    fun init() {
        db = FirebaseManager.getFirestore()
    }

    fun isAvailable(): Boolean = FirebaseManager.disponible && db != null

    fun syncEvento(context: Context, evento: EventoCalendario) {
        if (!isAvailable()) return
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        val data = hashMapOf(
            "id_local" to evento.id,
            "titulo" to evento.titulo,
            "descripcion" to evento.descripcion,
            "fecha" to evento.fecha,
            "hora" to evento.hora,
            "tipo" to evento.tipo,
            "userId" to userId,
            "syncedAt" to com.google.firebase.Timestamp.now()
        )

        db?.collection(COL_EVENTOS)
            ?.document("${userId}_${evento.id}")
            ?.set(data, SetOptions.merge())
            ?.addOnSuccessListener { Log.d(TAG, "Evento sincronizado: ${evento.titulo}") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error sync evento", e) }
    }

    fun eliminarEventoRemoto(evento: EventoCalendario) {
        if (!isAvailable()) return
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        db?.collection(COL_EVENTOS)
            ?.document("${userId}_${evento.id}")
            ?.delete()
    }

    fun escucharEventos(context: Context, userId: Int, onChange: (List<EventoCalendario>) -> Unit) {
        escuchaEventos?.remove()
        if (!isAvailable()) return

        escuchaEventos = db?.collection(COL_EVENTOS)
            ?.whereEqualTo("userId", userId)
            ?.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Error escuchando eventos", e)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val eventos = snapshots.documents.mapNotNull { doc ->
                        docToEvento(doc)
                    }
                    onChange(eventos)
                    guardarEventosLocal(context, eventos)
                }
            }
    }

    fun detenerEscucha() {
        escuchaEventos?.remove()
        escuchaFamilia?.remove()
    }

    // --- Familia ---

    fun syncMiembro(context: Context, miembro: MiembroFamiliar) {
        if (!isAvailable()) return
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        val data = hashMapOf(
            "id_local" to miembro.id,
            "nombre" to miembro.nombre,
            "email" to miembro.email,
            "telefono" to miembro.telefono,
            "rol" to miembro.rol,
            "mayorId" to miembro.mayorId,
            "userId" to userId,
            "syncedAt" to com.google.firebase.Timestamp.now()
        )

        db?.collection(COL_FAMILIA)
            ?.document("${userId}_${miembro.id}")
            ?.set(data, SetOptions.merge())
    }

    fun eliminarMiembroRemoto(miembro: MiembroFamiliar) {
        if (!isAvailable()) return
        val userId = SessionManager.getUserId()
        if (userId <= 0) return

        db?.collection(COL_FAMILIA)
            ?.document("${userId}_${miembro.id}")
            ?.delete()
    }

    fun escucharFamilia(context: Context, mayorId: Int, onChange: (List<MiembroFamiliar>) -> Unit) {
        escuchaFamilia?.remove()
        if (!isAvailable()) return

        escuchaFamilia = db?.collection(COL_FAMILIA)
            ?.whereEqualTo("mayorId", mayorId)
            ?.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Error escuchando familia", e)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val miembros = snapshots.documents.mapNotNull { doc ->
                        docToMiembro(doc)
                    }
                    onChange(miembros)
                    guardarMiembrosLocal(context, miembros)
                }
            }
    }

    // --- Conversión ---

    private fun docToEvento(doc: DocumentSnapshot): EventoCalendario? {
        return try {
            EventoCalendario(
                id = doc.getLong("id_local")?.toInt() ?: return null,
                titulo = doc.getString("titulo") ?: "",
                descripcion = doc.getString("descripcion") ?: "",
                fecha = doc.getString("fecha") ?: "",
                hora = doc.getString("hora") ?: "",
                tipo = doc.getString("tipo") ?: "personal",
                userId = doc.getLong("userId")?.toInt() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun docToMiembro(doc: DocumentSnapshot): MiembroFamiliar? {
        return try {
            MiembroFamiliar(
                id = doc.getLong("id_local")?.toInt() ?: return null,
                nombre = doc.getString("nombre") ?: "",
                email = doc.getString("email") ?: "",
                telefono = doc.getString("telefono") ?: "",
                rol = doc.getString("rol") ?: "viewer",
                mayorId = doc.getLong("mayorId")?.toInt() ?: 0,
                userId = doc.getLong("userId")?.toInt() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun guardarEventosLocal(context: Context, eventos: List<EventoCalendario>) {
        Thread {
            val db = AppDatabase.getInstance(context)
            runBlocking {
                for (evento in eventos) {
                    val existente = db.eventoCalendarioDao().getById(evento.id)
                    if (existente == null) {
                        db.eventoCalendarioDao().insert(evento)
                    }
                }
            }
        }.start()
    }

    private fun guardarMiembrosLocal(context: Context, miembros: List<MiembroFamiliar>) {
        Thread {
            val db = AppDatabase.getInstance(context)
            runBlocking {
                for (miembro in miembros) {
                    val existente = db.miembroFamiliarDao().getById(miembro.id)
                    if (existente == null) {
                        db.miembroFamiliarDao().insert(miembro)
                    }
                }
            }
        }.start()
    }
}
