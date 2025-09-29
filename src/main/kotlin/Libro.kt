package org.example

open class Libro(
    val titulo: String,
    val autor: String,
    val precioBase: Double,
    val diasPrestamo: Int
) {

    open fun costoFinal(): Double {
        return precioBase
    }

    open fun descripcion(): String {
        return "$titulo de $autor, Precio: $precioBase, Dias: $diasPrestamo"
    }

}