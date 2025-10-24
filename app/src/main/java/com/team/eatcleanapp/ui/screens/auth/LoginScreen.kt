package com.team.eatcleanapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onGoRegister: () -> Unit
) {
    Scaffold { inner ->
        Column(
            Modifier.padding(inner).fillMaxSize().padding(20.dp)
        ) {
            Text("Login Screen")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onLoginClick) { Text("Đăng nhập") }
            TextButton(onClick = onGoRegister) { Text("Đi đến Đăng ký") }
        }
    }
}