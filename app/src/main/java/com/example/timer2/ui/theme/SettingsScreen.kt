package com.example.timer2.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController,
    onPomodoroTimeChange: (Int) -> Unit,
    onThemeChange: (Boolean) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    isSoundEnabled: MutableState<Boolean>,
    isVibrationEnabled: MutableState<Boolean>,
    isDarkTheme: Boolean // New parameter to receive the current theme state
) {
    var showInputDialog by remember { mutableStateOf(false) }
    var inputMinutes by remember { mutableStateOf("") }

    // Define background color based on the theme
    val backgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Set the background color
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Timer Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = textColor // Set text color based on theme
        )

        Button(onClick = { showInputDialog = true }) {
            Text("Set Pomodoro Timer")
        }

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sound", color = textColor)
            Switch(
                checked = isSoundEnabled.value,
                onCheckedChange = {
                    isSoundEnabled.value = it
                    onSoundChange(it)
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Vibration", color = textColor)
            Switch(
                checked = isVibrationEnabled.value,
                onCheckedChange = {
                    isVibrationEnabled.value = it
                    onVibrationChange(it)
                }
            )
        }

        Button(onClick = { navController.navigateUp() }) {
            Text("Back")
        }

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
                        val minutes = inputMinutes.toIntOrNull() ?: 25
                        onPomodoroTimeChange(minutes * 60)
                        showInputDialog = false
                        inputMinutes = ""
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