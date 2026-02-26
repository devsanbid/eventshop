package com.example.eventshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.eventshop.navigation.AppNavGraph
import com.example.eventshop.ui.theme.EventBazaarTheme
import com.example.eventshop.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventBazaarTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                AppNavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}