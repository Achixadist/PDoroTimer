package com.example.timer2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer2.timer.PomodoroTimer

@Composable
fun PomodoroTimerScreen() {
    val pomodoroTimer = remember { PomodoroTimer() }

    val minutes = (pomodoroTimer.getTimeLeftInMillis() / 1000) / 60
    val seconds = (pomodoroTimer.getTimeLeftInMillis() / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = timeFormatted,
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (pomodoroTimer.isTimerRunning()) {
                pomodoroTimer.pauseTimer()
            } else {
                pomodoroTimer.startTimer(
                    onTick = { /* Handle onTick */ },
                    onFinish = { /* Handle onFinish */ }
                )
            }
        }) {
            Text(text = if (pomodoroTimer.isTimerRunning()) "Pause" else "Start")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { pomodoroTimer.resetTimer() }) {
            Text(text = "Reset")
        }
    }
}
