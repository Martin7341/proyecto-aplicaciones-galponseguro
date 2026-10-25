package com.example.galponseguro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.galponseguro.ui.screen.AccionScreen
import com.example.galponseguro.ui.screen.AlertasScreen
import com.example.galponseguro.ui.screen.HomeScreen
import com.example.galponseguro.ui.screen.PerfilScreen
import com.example.galponseguro.ui.viewmodel.AlertaViewModel
import com.example.galponseguro.ui.viewmodel.CamaraGaleriaViewModel
import com.example.galponseguro.ui.viewmodel.GalponViewModel
import com.example.galponseguro.ui.viewmodel.PerfilViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val galponViewModel: GalponViewModel = viewModel()
    val alertaViewModel: AlertaViewModel = viewModel()
    val imagenViewModel: CamaraGaleriaViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    val titulo = when (currentRoute) {
        AppRoutes.ALERTAS -> "Alertas"
        AppRoutes.ACCION -> "Acción correctiva"
        AppRoutes.PERFIL -> "Perfil"
        else -> "Inicio"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "GalponSeguro",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(24.dp)
                )
                NavigationDrawerItem(
                    selected = currentRoute == AppRoutes.HOME,
                    onClick = {
                        navController.navigate(AppRoutes.HOME)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationDrawerItem(
                    selected = currentRoute == AppRoutes.ALERTAS,
                    onClick = {
                        navController.navigate(AppRoutes.ALERTAS)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "Alertas") },
                    label = { Text("Alertas") }
                )
                NavigationDrawerItem(
                    selected = currentRoute == AppRoutes.PERFIL,
                    onClick = {
                        navController.navigate(AppRoutes.PERFIL)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(titulo) },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.HOME,
                        onClick = { navController.navigate(AppRoutes.HOME) },
                        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.ALERTAS,
                        onClick = { navController.navigate(AppRoutes.ALERTAS) },
                        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "Alertas") },
                        label = { Text("Alertas") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.PERFIL,
                        onClick = { navController.navigate(AppRoutes.PERFIL) },
                        icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = AppRoutes.HOME,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(AppRoutes.HOME) {
                    HomeScreen(viewModel = galponViewModel)
                }
                composable(AppRoutes.ALERTAS) {
                    AlertasScreen(
                        viewModel = alertaViewModel,
                        onAccionClick = { navController.navigate(AppRoutes.ACCION) }
                    )
                }
                composable(AppRoutes.ACCION) {
                    AccionScreen(
                        viewModel = alertaViewModel,
                        imagenViewModel = imagenViewModel,
                        onGuardado = { navController.popBackStack() }
                    )
                }
                composable(AppRoutes.PERFIL) {
                    PerfilScreen(viewModel = perfilViewModel)
                }
            }
        }
    }
}