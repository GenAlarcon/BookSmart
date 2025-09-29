package org.example

// Permite definir todos los estados del préstamo en el lugar (según chat gpt)
sealed class EstadoPrestamo {
    object Pendiente : EstadoPrestamo()
    object EnPrestamo : EstadoPrestamo()
    object Devuelto : EstadoPrestamo()
    data class Error(val msg: String) : EstadoPrestamo()
}
