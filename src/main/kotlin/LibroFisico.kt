package org.example

class LibroFisico(
    titulo: String,
    autor: String,
    precioBase: Double,
    diasPrestamo: Int,
    val esReferencia: Boolean
) : Libro(titulo, autor, precioBase, diasPrestamo) {

    // Calcula el costo final: 0 si es de referencia
    override fun costoFinal(): Double {
        return if (esReferencia) 0.0 else precioBase
    }

    // Descripción detallada del libro
    override fun descripcion(): String {
        val tipo = if (esReferencia) "Referencia" else "Normal"
        val precioFormateado = "%.2f".format(costoFinal())
        return "$titulo de $autor ($tipo), Precio: $precioFormateado, Dias: $diasPrestamo"
    }
}
