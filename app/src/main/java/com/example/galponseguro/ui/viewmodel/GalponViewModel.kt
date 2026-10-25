package com.example.galponseguro.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.galponseguro.data.Alerta
import com.example.galponseguro.data.AlertaRepository
import com.example.galponseguro.data.AppDatabase
import com.example.galponseguro.data.model.EstadoGalpon
import com.example.galponseguro.data.model.Galpon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GalponViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AlertaRepository(
        AppDatabase.getDatabase(application).alertaDao()
    )

    private val _galpones = MutableStateFlow(
        listOf(
            Galpon(id = 1, nombre = "Galpón 1", temperatura = 24.0, humedad = 55),
            Galpon(id = 2, nombre = "Galpón 2", temperatura = 27.0, humedad = 60),
            Galpon(id = 3, nombre = "Galpón 3", temperatura = 30.0, humedad = 65)
        )
    )
    val galpones: StateFlow<List<Galpon>> = _galpones.asStateFlow()

    fun subirTemperatura(id: Int) = cambiarMedicion(id, 1.0, 0)

    fun bajarTemperatura(id: Int) = cambiarMedicion(id, -1.0, 0)

    fun subirHumedad(id: Int) = cambiarMedicion(id, 0.0, 5)

    fun bajarHumedad(id: Int) = cambiarMedicion(id, 0.0, -5)

    private fun cambiarMedicion(id: Int, cambioTemp: Double, cambioHum: Int) {
        val actual = _galpones.value.firstOrNull { it.id == id } ?: return

        val nuevo = actual.copy(
            temperatura = actual.temperatura + cambioTemp,
            humedad = (actual.humedad + cambioHum).coerceIn(0, 100)
        )

        _galpones.update { lista ->
            lista.map { galpon ->
                if (galpon.id == id) nuevo else galpon
            }
        }

        if (nuevo.estado == EstadoGalpon.CRITICO &&
            actual.estado != EstadoGalpon.CRITICO
        ) {
            generarAlerta(nuevo)
        }
    }

    private fun generarAlerta(galpon: Galpon) {
        viewModelScope.launch {
            repository.insertar(
                Alerta(
                    galponNombre = galpon.nombre,
                    temperatura = galpon.temperatura,
                    humedad = galpon.humedad,
                    nivel = "Crítico",
                    fecha = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date())
                )
            )
        }
    }
}