package com.example.galponseguro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.galponseguro.ui.navigation.AppNavigation
import com.example.galponseguro.ui.theme.GalponSeguroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalponSeguroTheme {
                AppNavigation()
            }
        }
    }
}