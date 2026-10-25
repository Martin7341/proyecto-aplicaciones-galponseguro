package com.example.galponseguro.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.galponseguro.ui.viewmodel.AlertaViewModel
import com.example.galponseguro.ui.viewmodel.CamaraGaleriaViewModel

@Composable
fun AccionScreen(
    viewModel: AlertaViewModel,
    imagenViewModel: CamaraGaleriaViewModel,
    onGuardado: () -> Unit
) {
    val alerta by viewModel.alertaSeleccionada.collectAsStateWithLifecycle()
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()
    val errores by viewModel.errores.collectAsStateWithLifecycle()
    val imagen by imagenViewModel.uiState.collectAsStateWithLifecycle()

    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imagenViewModel.imagenDesdeCamara(bitmap)
        }
    }

    val galeria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imagenViewModel.imagenDesdeGaleria(uri)
        }
    }

    val actual = alerta

    if (actual == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Selecciona una alerta desde el historial")
        }
    } else {
        val bitmap = imagen.bitmap
        val uri = imagen.uri

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Acción correctiva",
                style = MaterialTheme.typography.headlineSmall
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "${actual.galponNombre} - ${actual.nivel}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Medición: ${actual.temperatura} °C / ${actual.humedad} %")
                    Text(text = "Fecha: ${actual.fecha}")
                }
            }

            OutlinedTextField(
                value = formulario.responsable,
                onValueChange = viewModel::cambiarResponsable,
                label = { Text("Responsable") },
                isError = errores.responsable != null,
                supportingText = {
                    errores.responsable?.let { Text(it) }
                },
                trailingIcon = {
                    if (errores.responsable != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formulario.accion,
                onValueChange = viewModel::cambiarAccion,
                label = { Text("Acción realizada") },
                isError = errores.accion != null,
                supportingText = {
                    errores.accion?.let { Text(it) }
                },
                trailingIcon = {
                    if (errores.accion != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error"
                        )
                    }
                },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Evidencia (opcional)",
                style = MaterialTheme.typography.titleMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { camara.launch(null) }) {
                    Text("Tomar foto")
                }
                Button(onClick = { galeria.launch("image/*") }) {
                    Text("Elegir de galería")
                }
            }

            when {
                bitmap != null -> {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Fotografía tomada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                uri != null -> {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (viewModel.guardarAccion()) {
                        imagenViewModel.limpiarImagen()
                        onGuardado()
                    }
                }
            ) {
                Text("Guardar acción")
            }
        }
    }
}