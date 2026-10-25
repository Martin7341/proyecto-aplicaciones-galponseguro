package com.example.galponseguro.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.galponseguro.data.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val nombre: String = "",
    val correo: String = ""
)

data class PerfilErrores(
    val nombre: String? = null,
    val correo: String? = null
)

class PerfilViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = PreferenciasRepository(application)

    private val _formulario = MutableStateFlow(PerfilUiState())
    val formulario: StateFlow<PerfilUiState> = _formulario.asStateFlow()

    private val _errores = MutableStateFlow(PerfilErrores())
    val errores: StateFlow<PerfilErrores> = _errores.asStateFlow()

    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado.asStateFlow()

    init {
        viewModelScope.launch {
            _formulario.value = PerfilUiState(
                nombre = repositorio.nombre.first(),
                correo = repositorio.correo.first()
            )
        }
    }

    fun cambiarNombre(valor: String) {
        _formulario.update { it.copy(nombre = valor) }
        _guardado.value = false
    }

    fun cambiarCorreo(valor: String) {
        _formulario.update { it.copy(correo = valor) }
        _guardado.value = false
    }

    private fun validarFormulario(): Boolean {
        val f = _formulario.value

        val errorNombre = when {
            f.nombre.isBlank() -> "El nombre es obligatorio"
            f.nombre.trim().length < 3 -> "Mínimo 3 caracteres"
            else -> null
        }

        val errorCorreo = when {
            f.correo.isBlank() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(f.correo.trim()).matches() -> "Correo no válido"
            else -> null
        }

        _errores.value = PerfilErrores(
            nombre = errorNombre,
            correo = errorCorreo
        )

        return errorNombre == null && errorCorreo == null
    }

    fun guardar() {
        if (!validarFormulario()) {
            _guardado.value = false
            return
        }
        val f = _formulario.value
        viewModelScope.launch {
            repositorio.guardarNombre(f.nombre.trim())
            repositorio.guardarCorreo(f.correo.trim())
            _guardado.value = true
        }
    }
}