package com.example.galponseguro.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "preferencias_usuario"
)

class PreferenciasRepository(
    private val context: Context
) {
    companion object {
        private val NOMBRE = stringPreferencesKey("nombre_usuario")
        private val CORREO = stringPreferencesKey("correo_usuario")
    }

    val nombre = context.dataStore.data.map { preferencias ->
        preferencias[NOMBRE] ?: ""
    }

    val correo = context.dataStore.data.map { preferencias ->
        preferencias[CORREO] ?: ""
    }

    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { preferencias ->
            preferencias[NOMBRE] = nombre
        }
    }

    suspend fun guardarCorreo(correo: String) {
        context.dataStore.edit { preferencias ->
            preferencias[CORREO] = correo
        }
    }
}