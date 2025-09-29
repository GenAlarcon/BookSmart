package org.example

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("----------- SISTEMA BOOKSMART ----------")
    println("--- Catalogo disponible:")

    // Inicializar catálogo
    GestorPrestamos.inicializarCatalogo()
    GestorPrestamos.catalogo.forEachIndexed { index, libro ->
        println("${index + 1}. ${libro.descripcion()}")
    }

    // Solicitar selección de libros
    print("\nSeleccione libros para prestamo (numeros separados por coma): ")
    val inputLibros = readLine() ?: ""
    val indicesSeleccionados = inputLibros.split(",")
        .mapNotNull { it.trim().toIntOrNull()?.minus(1) }

    // Solicitar tipo de usuario
    print("Tipo de usuario (estudiante/docente/externo): ")
    val tipoUsuarioInput = readLine()?.lowercase() ?: "externo"
    val tipoUsuario = when (tipoUsuarioInput) {
        "estudiante" -> TipoUsuario.ESTUDIANTE
        "docente" -> TipoUsuario.DOCENTE
        else -> TipoUsuario.EXTERNO
    }

    println("\nValidando solicitud...")

    // Lista explícita con tipo para evitar "Cannot infer type"
    val prestamosSeleccionados: MutableList<Prestamo> = mutableListOf()

    for (idx in indicesSeleccionados) {
        if (idx in GestorPrestamos.catalogo.indices) {
            val libro = GestorPrestamos.catalogo[idx]
            val prestamo = GestorPrestamos.registrarPrestamo(libro, tipoUsuario)
            if (prestamo.estado is EstadoPrestamo.Error) {
                println("- Libro '${libro.titulo}' no se puede prestar: ${(prestamo.estado as EstadoPrestamo.Error).msg}")
            } else {
                prestamosSeleccionados.add(prestamo)
            }
        }
    }

    println("\nProcesando prestamos...")

    // Usar for normal porque procesarPrestamo es suspend
    for (prestamo in prestamosSeleccionados) {
        GestorPrestamos.procesarPrestamo(prestamo)
        GestorPrestamos.mostrarResumen(prestamo)
    }

    println("\nTodos los prestamos procesados.")
}
