package com.example.timer2.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun PomodoroTimerScreen(
    taskName: String,
    onBack: () -> Unit // Callback to navigate back to the to-do list
) {
    var timeLeft by remember { mutableStateOf(25 * 60 * 1000L) } // 25 minutes in milliseconds
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0 && isRunning) {
                delay(1000L) // Decrease the timer every second
                timeLeft -= 1000L
            }
            if (timeLeft == 0L) {
                isRunning = false // Stop when time is up
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Task: $taskName",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the countdown timer
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to toggle the Pomodoro timer (Start, Pause, Resume)
        Button(onClick = { isRunning = !isRunning }) {
            Text(if (isRunning) "Pause Pomodoro" else "Start Pomodoro")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to reset the Pomodoro timer
        Button(onClick = {
            isRunning = false
            timeLeft = 25 * 60 * 1000L
        }) {
            Text("Reset Timer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back to To-Do List")
        }
    }
}
