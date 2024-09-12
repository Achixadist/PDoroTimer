package com.example.timer2.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController,
    onPomodoroTimeChange: (Int) -> Unit, // Ensure this parameter is defined
    onThemeChange: (Boolean) -> Unit
) {
    var showInputDialog by remember { mutableStateOf(false) }
    var inputMinutes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Timer Settings", style = MaterialTheme.typography.headlineMedium)

        // Button to open input dialog for Pomodoro timer
        Button(onClick = { showInputDialog = true }) {
            Text("Set Pomodoro Timer")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Theme toggle buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { onThemeChange(false) }) {
                Text("Light Mode")
            }
            Button(onClick = { onThemeChange(true) }) {
                Text("Dark Mode")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigateUp() }) {
            Text("Back")
        }

        // Input Dialog for Pomodoro Time
        if (showInputDialog) {
            AlertDialog(
                onDismissRequest = { showInputDialog = false },
                title = { Text("Set Pomodoro Duration") },
                text = {
                    Column {
                        Text("Enter duration in minutes:")
                        TextField(
                            value = inputMinutes,
                            onValueChange = { inputMinutes = it },
                            placeholder = { Text("25") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val minutes = inputMinutes.toIntOrNull() ?: 25 // Default to 25 if input is invalid
                        onPomodoroTimeChange(minutes * 60) // Convert minutes to seconds
                        showInputDialog = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showInputDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
