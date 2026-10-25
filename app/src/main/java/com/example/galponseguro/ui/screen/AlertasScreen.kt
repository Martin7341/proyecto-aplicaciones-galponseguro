package com.example.galponseguro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.galponseguro.ui.viewmodel.AlertaViewModel

@Composable
fun AlertasScreen(
    viewModel: AlertaViewModel,
    onAccionClick: () -> Unit
) {
    val alertas by viewModel.alertas.collectAsStateWithLifecycle()

    if (alertas.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No hay alertas registradas")
            Text(text = "Sube la temperatura de un galpón en Inicio para generar una")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Historial de alertas (${alertas.size})",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(items = alertas, key = { it.id }) { alerta ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${alerta.galponNombre} - ${alerta.nivel}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Medición: ${alerta.temperatura} °C / ${alerta.humedad} %")
                        Text(text = "Fecha: ${alerta.fecha}")
                        Text(
                            text = "Estado: " +
                                    if (alerta.atendida) "Atendida" else "Pendiente"
                        )
                        if (alerta.atendida) {
                            Text(text = "Responsable: ${alerta.responsable}")
                            Text(text = "Acción: ${alerta.accion}")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.seleccionar(alerta)
                                    onAccionClick()
                                }
                            ) {
                                Text(
                                    if (alerta.atendida) "Editar acción"
                                    else "Registrar acción"
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.eliminar(alerta) }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}