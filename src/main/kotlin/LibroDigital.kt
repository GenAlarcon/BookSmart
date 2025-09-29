class LibroDigital(
    titulo: String,
    autor: String,
    precioBase: Double,
    diasPrestamo: Int,
    val drm: Boolean = false
) : Libro(titulo, autor, precioBase, diasPrestamo) {

    override fun costoFinal(): Double {
        return if (drm) precioBase * 1.2 else precioBase
    }

    override fun descripcion(): String {
        val estadoDRM = if (drm) "Con DRM" else "Sin DRM"
        return "$titulo de $autor, Precio: ${costoFinal()}, Días: $diasPrestamo, ($estadoDRM)"
    }
}
