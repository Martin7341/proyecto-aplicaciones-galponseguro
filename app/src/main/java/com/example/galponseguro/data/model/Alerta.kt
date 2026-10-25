package com.example.galponseguro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alertas")
data class Alerta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val galponNombre: String,
    val temperatura: Double,
    val humedad: Int,
    val nivel: String,
    val fecha: String,
    val atendida: Boolean = false,
    val responsable: String = "",
    val accion: String = ""
)