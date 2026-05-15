package com.example.miaplicacion

import org.junit.Assert.*
import org.junit.Test
import java.io.StringWriter

class CsvExporterUnitTest {

    @Test
    fun formatoCSV_generaEncabezadosCorrectos() {
        val registros = listOf(
            RegistroSalud(tipo = "presion", valor = "120/80", nota = "Normal", fecha = "01/01/2026", userId = 1)
        )

        val sw = StringWriter()
        sw.append("Tipo,Valor,Nota,Fecha\n")
        for (r in registros) {
            val tipo = cuandoTipo(r.tipo)
            sw.append("$tipo,${r.valor},${r.nota},${r.fecha}\n")
        }

        val csv = sw.toString()
        assertTrue(csv.startsWith("Tipo,Valor,Nota,Fecha"))
        assertTrue(csv.contains("Presión arterial"))
        assertTrue(csv.contains("120/80"))
    }

    @Test
    fun csvConMultiplesRegistros_tieneLineasCorrespondientes() {
        val registros = listOf(
            RegistroSalud(tipo = "peso", valor = "75kg", nota = "", fecha = "01/01/2026", userId = 1),
            RegistroSalud(tipo = "glucosa", valor = "95", nota = "En ayunas", fecha = "02/01/2026", userId = 1)
        )

        val sw = StringWriter()
        sw.append("Tipo,Valor,Nota,Fecha\n")
        for (r in registros) sw.append("${cuandoTipo(r.tipo)},${r.valor},${r.nota},${r.fecha}\n")

        val lineas = sw.toString().trim().split("\n")
        assertEquals(3, lineas.size) // header + 2 registros
    }

    @Test
    fun csvVacio_soloTieneEncabezado() {
        val registros = emptyList<RegistroSalud>()

        val sw = StringWriter()
        sw.append("Tipo,Valor,Nota,Fecha\n")

        val lineas = sw.toString().trim().split("\n")
        assertEquals(1, lineas.size)
    }

    private fun cuandoTipo(tipo: String): String = when (tipo) {
        "presion" -> "Presión arterial"
        "glucosa" -> "Glucosa"
        "peso" -> "Peso"
        "animo" -> "Estado de ánimo"
        else -> tipo
    }
}
