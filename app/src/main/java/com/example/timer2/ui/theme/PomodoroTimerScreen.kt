package com.example.timer2.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timer2.timer.TimerViewModel

@Composable
fun PomodoroTimerScreen(
    viewModel: TimerViewModel = viewModel()
) {
    // Observing state from ViewModel
    val timeLeftInMillis by viewModel.timeLeft.collectAsState()
    val isTimerRunning by viewModel.isRunning.collectAsState()

    val minutes = (timeLeftInMillis / 1000) / 60
    val seconds = (timeLeftInMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
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
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isTimerRunning) viewModel.pauseTimer()
                    else viewModel.startPomodoroTimer() // Calls the start method for Pomodoro
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF6200EE), RoundedCornerShape(50))
            ) {
                Text(
                    text = if (isTimerRunning) "Pause Pomodoro" else "Start Pomodoro",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.startShortBreak() // Calls the start method for Short Break
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF03DAC5), RoundedCornerShape(50))
            ) {
                Text(
                    text = "Start Short Break",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.startLongBreak() // Calls the start method for Long Break
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF018786), RoundedCornerShape(50))
            ) {
                Text(
                    text = "Start Long Break",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.resetTimer() }, // Resets the timer
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFFB00020), RoundedCornerShape(50))
            ) {
                Text(
                    text = "Reset",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}
