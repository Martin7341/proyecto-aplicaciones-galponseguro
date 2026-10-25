package com.example.galponseguro.data

import kotlinx.coroutines.flow.Flow

class AlertaRepository(
    private val dao: AlertaDao
) {
    fun obtenerTodas(): Flow<List<Alerta>> {
        return dao.obtenerTodas()
    }

    suspend fun insertar(alerta: Alerta) {
        dao.insertar(alerta)
    }

    suspend fun actualizar(alerta: Alerta) {
        dao.actualizar(alerta)
    }

    suspend fun eliminar(alerta: Alerta) {
        dao.eliminar(alerta)
    }
}