package com.example.galponseguro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertaDao {

    @Insert
    suspend fun insertar(alerta: Alerta)

    @Query("SELECT * FROM alertas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Alerta>>

    @Update
    suspend fun actualizar(alerta: Alerta)

    @Delete
    suspend fun eliminar(alerta: Alerta)
}