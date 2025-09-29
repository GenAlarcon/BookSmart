class LibroFisico(
    titulo: String,
    autor: String,
    precioBase: Double,
    diasPrestamo: Int,
    val esReferencia: Boolean
) : Libro(titulo, autor, precioBase, diasPrestamo) {

    override fun costoFinal(): Double {
        return if (esReferencia) 0.0 else precioBase
    }

    override fun descripcion(): String {
        val tipo = if (esReferencia) "Referencia" else "Normal"
        return "$titulo de $autor, ($tipo), Precio: ${costoFinal()}, Días: $diasPrestamo"
    }
}
