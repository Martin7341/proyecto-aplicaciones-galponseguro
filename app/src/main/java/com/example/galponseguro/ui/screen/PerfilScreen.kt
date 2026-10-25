package com.example.galponseguro.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.galponseguro.ui.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(viewModel: PerfilViewModel) {
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()
    val errores by viewModel.errores.collectAsStateWithLifecycle()
    val guardado by viewModel.guardado.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = formulario.nombre,
            onValueChange = viewModel::cambiarNombre,
            label = { Text("Nombre") },
            isError = errores.nombre != null,
            supportingText = {
                errores.nombre?.let { Text(it) }
            },
            trailingIcon = {
                if (errores.nombre != null) {
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
            value = formulario.correo,
            onValueChange = viewModel::cambiarCorreo,
            label = { Text("Correo electrónico") },
            isError = errores.correo != null,
            supportingText = {
                errores.correo?.let { Text(it) }
            },
            trailingIcon = {
                if (errores.correo != null) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error"
                    )
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = viewModel::guardar
        ) {
            Text("Guardar perfil")
        }

        AnimatedVisibility(visible = guardado) {
            Text(
                text = "Perfil guardado correctamente",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}