package com.example.galponseguro.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.galponseguro.data.model.EstadoGalpon
import com.example.galponseguro.data.model.Galpon
import com.example.galponseguro.ui.viewmodel.GalponViewModel

@Composable
fun HomeScreen(viewModel: GalponViewModel) {
    val galpones by viewModel.galpones.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Estado de los galpones",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(12.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (maxWidth < 600.dp) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = galpones, key = { it.id }) { galpon ->
                        GalponCard(galpon = galpon, viewModel = viewModel)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = galpones, key = { it.id }) { galpon ->
                        GalponCard(galpon = galpon, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun GalponCard(
    galpon: Galpon,
    viewModel: GalponViewModel
) {
    val colorEstado by animateColorAsState(
        targetValue = when (galpon.estado) {
            EstadoGalpon.NORMAL -> Color(0xFFDDF7E8)
            EstadoGalpon.ADVERTENCIA -> Color(0xFFFFF3C4)
            EstadoGalpon.CRITICO -> Color(0xFFFFD6D6)
        },
        label = "colorEstado"
    )

    val textoEstado = when (galpon.estado) {
        EstadoGalpon.NORMAL -> "Normal"
        EstadoGalpon.ADVERTENCIA -> "Advertencia"
        EstadoGalpon.CRITICO -> "Crítico"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorEstado,
            contentColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = galpon.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Temperatura: ${galpon.temperatura} °C")
            Text(text = "Humedad: ${galpon.humedad} %")
            Text(text = "Estado: $textoEstado")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.bajarTemperatura(galpon.id) }) {
                    Text("Temp -1")
                }
                Button(onClick = { viewModel.subirTemperatura(galpon.id) }) {
                    Text("Temp +1")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.bajarHumedad(galpon.id) }) {
                    Text("Hum -5")
                }
                Button(onClick = { viewModel.subirHumedad(galpon.id) }) {
                    Text("Hum +5")
                }
            }
        }
    }
}