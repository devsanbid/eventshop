package com.example.eventshop.view.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.eventshop.utils.Resource
import com.example.eventshop.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToUser: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        if (authViewModel.isLoggedIn) {
            authViewModel.fetchCurrentUser()
        } else {
            onNavigateToLogin()
        }
    }

    LaunchedEffect(currentUser) {
        when (val state = currentUser) {
            is Resource.Success -> {
                if (state.data.role == "admin") {
                    onNavigateToAdmin()
                } else {
                    onNavigateToUser()
                }
            }
            is Resource.Error -> {
                onNavigateToLogin()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Celebration,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "EventBazaar",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}
