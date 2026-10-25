package com.example.galponseguro.data.model

enum class EstadoGalpon { NORMAL, ADVERTENCIA, CRITICO }

data class Galpon(
    val id: Int,
    val nombre: String,
    val temperatura: Double,
    val humedad: Int
) {
    val estado: EstadoGalpon
        get() = when {
            temperatura >= 32 || humedad >= 80 -> EstadoGalpon.CRITICO
            temperatura >= 28 || humedad >= 70 -> EstadoGalpon.ADVERTENCIA
            else -> EstadoGalpon.NORMAL
        }
}