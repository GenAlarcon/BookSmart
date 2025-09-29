package org.example

import kotlinx.coroutines.*

object GestorPrestamos {

    val catalogo: MutableList<Libro> = mutableListOf()
    val prestamos: MutableList<Prestamo> = mutableListOf()

    fun inicializarCatalogo() {
        catalogo.clear()
        catalogo.add(LibroFisico("Estructuras de Datos", "Goodrich", 12990.0, diasPrestamo = 7, esReferencia = false))
        catalogo.add(LibroFisico("Diccionario Enciclopedico", "Varios", 15990.0, diasPrestamo = 0, esReferencia = true))
        catalogo.add(LibroDigital("Programacion en Kotlin", "JetBrains", 9990.0, diasPrestamo = 10, drm = true))
        catalogo.add(LibroDigital("Algoritmos Basicos", "Cormen", 11990.0, diasPrestamo = 10, drm = false))
    }

    fun registrarPrestamo(libro: Libro, tipoUsuario: TipoUsuario, diasRetraso: Int = 0): Prestamo {
        val estadoInicial = when {
            libro is LibroFisico && libro.esReferencia -> EstadoPrestamo.Error("No se puede prestar libro de referencia")
            libro.diasPrestamo <= 0 -> EstadoPrestamo.Error("Dias de prestamo inválidos")
            else -> EstadoPrestamo.Pendiente
        }
        val prestamo = Prestamo(libro, tipoUsuario, estadoInicial, diasRetraso)
        prestamos.add(prestamo)
        return prestamo
    }

    fun calcularValores(prestamo: Prestamo, tarifaMulta: Double = 500.0): Triple<Double, Double, Double> {
        val subtotal = prestamo.libro.costoFinal()
        val descuento = subtotal * prestamo.tipoUsuario.descuento
        val multa = if (prestamo.diasRetraso > 0) prestamo.diasRetraso * tarifaMulta else 0.0
        return Triple(subtotal, descuento, multa)
    }

    suspend fun procesarPrestamo(prestamo: Prestamo) {
        if (prestamo.estado is EstadoPrestamo.Error) {
            println("Error en prestamo: ${(prestamo.estado as EstadoPrestamo.Error).msg}")
            return
        }

        println("\nProcesando prestamo del libro '${prestamo.libro.titulo}'...")
        prestamo.estado = EstadoPrestamo.Pendiente
        println("Estado: Pendiente")
        delay(2000)

        prestamo.estado = EstadoPrestamo.EnPrestamo
        println("Estado: En Prestamo")
        delay(3000)

        prestamo.estado = EstadoPrestamo.Devuelto
        println("Estado: Devuelto")
    }

    fun mostrarResumen(prestamo: Prestamo, tarifaMulta: Double = 500.0) {
        val (subtotal, descuento, multa) = calcularValores(prestamo, tarifaMulta)
        val total = subtotal - descuento + multa

        println("\n===== RESUMEN DEL PRÉSTAMO =====")
        println(prestamo.libro.descripcion())
        println("Tipo de usuario: ${prestamo.tipoUsuario.nombre}")
        println("Subtotal: $${"%.2f".format(subtotal)}")
        println("Descuento (${(prestamo.tipoUsuario.descuento * 100).toInt()}%): -$${"%.2f".format(descuento)}")
        println("Multa: $${"%.2f".format(multa)}")
        println("TOTAL A PAGAR: $${"%.2f".format(total)}")
        println("Estado final: ${when (prestamo.estado) {
            is EstadoPrestamo.Pendiente -> "Pendiente"
            is EstadoPrestamo.EnPrestamo -> "En Prestamo"
            is EstadoPrestamo.Devuelto -> "Devuelto"
            is EstadoPrestamo.Error -> "Error: ${(prestamo.estado as EstadoPrestamo.Error).msg}"
        }}")
        println("================================")
    }
}

// CLASES Y ENUMS
data class Prestamo(
    val libro: Libro,
    val tipoUsuario: TipoUsuario,
    var estado: EstadoPrestamo,
    val diasRetraso: Int = 0
)

enum class TipoUsuario(val nombre: String, val descuento: Double) {
    ESTUDIANTE("Estudiante", 0.10),
    DOCENTE("Docente", 0.15),
    EXTERNO("Externo", 0.0)
}
