package com.example.miaplicacion

object TrabajadorManager {

    private var idContador = 1
    private val trabajadores = mutableListOf<Trabajador>()

    fun agregarTrabajador(t: Trabajador) {
        trabajadores.add(t)
    }

    fun registrarNuevoTrabajador(
        nombre: String,
        apellido: String,
        dni: String = "",
        email: String = "",
        telefono: String = ""
    ): Trabajador {
        val nuevoTrabajador = Trabajador(
            idTrabajador = idContador,
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            email = email,
            telefono = telefono
        )
        trabajadores.add(nuevoTrabajador)
        idContador++
        return nuevoTrabajador
    }

    fun getTrabajadores(): List<Trabajador> = trabajadores

    fun filtrarTrabajadoresPorID(query: String): List<Trabajador> {
        if (query.isBlank()) return trabajadores
        val idBusqueda = query.toIntOrNull() ?: return emptyList()
        return trabajadores.filter { it.idTrabajador == idBusqueda }
    }
}
