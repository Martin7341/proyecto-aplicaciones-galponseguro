package com.example.galponseguro.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.galponseguro.data.Alerta
import com.example.galponseguro.data.AlertaRepository
import com.example.galponseguro.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccionUiState(
    val responsable: String = "",
    val accion: String = ""
)

data class AccionErrores(
    val responsable: String? = null,
    val accion: String? = null
)

class AlertaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AlertaRepository(
        AppDatabase.getDatabase(application).alertaDao()
    )

    val alertas: StateFlow<List<Alerta>> = repository.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _alertaSeleccionada = MutableStateFlow<Alerta?>(null)
    val alertaSeleccionada: StateFlow<Alerta?> = _alertaSeleccionada.asStateFlow()

    private val _formulario = MutableStateFlow(AccionUiState())
    val formulario: StateFlow<AccionUiState> = _formulario.asStateFlow()

    private val _errores = MutableStateFlow(AccionErrores())
    val errores: StateFlow<AccionErrores> = _errores.asStateFlow()

    fun seleccionar(alerta: Alerta) {
        _alertaSeleccionada.value = alerta
        _formulario.value = AccionUiState(
            responsable = alerta.responsable,
            accion = alerta.accion
        )
        _errores.value = AccionErrores()
    }

    fun cambiarResponsable(valor: String) {
        _formulario.update { it.copy(responsable = valor) }
    }

    fun cambiarAccion(valor: String) {
        _formulario.update { it.copy(accion = valor) }
    }

    fun validarFormulario(): Boolean {
        val f = _formulario.value

        val errorResponsable = when {
            f.responsable.isBlank() -> "El responsable es obligatorio"
            f.responsable.trim().length < 3 -> "Mínimo 3 caracteres"
            else -> null
        }

        val errorAccion = when {
            f.accion.isBlank() -> "Describe la acción realizada"
            f.accion.trim().length < 10 -> "Mínimo 10 caracteres"
            else -> null
        }

        _errores.value = AccionErrores(
            responsable = errorResponsable,
            accion = errorAccion
        )

        return errorResponsable == null && errorAccion == null
    }

    fun guardarAccion(): Boolean {
        val alerta = _alertaSeleccionada.value ?: return false
        if (!validarFormulario()) return false

        val f = _formulario.value
        viewModelScope.launch {
            repository.actualizar(
                alerta.copy(
                    responsable = f.responsable.trim(),
                    accion = f.accion.trim(),
                    atendida = true
                )
            )
        }
        return true
    }

    fun eliminar(alerta: Alerta) {
        viewModelScope.launch {
            repository.eliminar(alerta)
        }
    }
}