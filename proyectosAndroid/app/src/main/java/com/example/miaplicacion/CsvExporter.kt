package com.example.miaplicacion

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    fun exportarRegistrosSalud(context: Context, registros: List<RegistroSalud>) {
        if (registros.isEmpty()) {
            Toast.makeText(context, "No hay registros para exportar", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val carpeta = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: context.filesDir
            if (!carpeta.exists()) carpeta.mkdirs()

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val archivo = File(carpeta, "historial_salud_$timestamp.csv")

            FileWriter(archivo).use { writer ->
                writer.append("Tipo,Valor,Nota,Fecha\n")
                for (r in registros) {
                    val tipo = when (r.tipo) {
                        "presion" -> "Presión arterial"
                        "glucosa" -> "Glucosa"
                        "peso" -> "Peso"
                        "animo" -> "Estado de ánimo"
                        else -> r.tipo
                    }
                    writer.append("$tipo,${r.valor},${r.nota},${r.fecha}\n")
                }
            }

            compartirArchivo(context, archivo)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al exportar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun compartirArchivo(context: Context, archivo: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Compartir historial de salud"))
    }
}
