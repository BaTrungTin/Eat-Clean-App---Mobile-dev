package com.team.eatcleanapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onGoLogin: () -> Unit
) {
    Scaffold { inner ->
        Column(
            Modifier.padding(inner).fillMaxSize().padding(20.dp)
        ) {
            Text("Register Screen")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRegisterClick) { Text("Đăng ký") }
            TextButton(onClick = onGoLogin) { Text("Đi đến Đăng nhập") }
        }
    }
}