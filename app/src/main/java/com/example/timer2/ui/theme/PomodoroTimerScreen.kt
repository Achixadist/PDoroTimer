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
    viewModel: TimerViewModel = viewModel() // Initializes the ViewModel instance
) {
    // Correctly collect the state values from the ViewModel
    val timeLeftInMillis by viewModel.timeLeft.collectAsState() // Collect the time left state
    val isTimerRunning by viewModel.isRunning.collectAsState() // Collect the running state

    val minutes = (timeLeftInMillis / 1000) / 60
    val seconds = (timeLeftInMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5) // Light gray background
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
                fontSize = 72.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isTimerRunning) {
                        viewModel.pauseTimer()
                    } else {
                        viewModel.startTimer(
                            onTick = { /* Handle onTick */ },
                            onFinish = { /* Handle onFinish */ }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF6200EE), RoundedCornerShape(50)) // Purple button
            ) {
                Text(
                    text = if (isTimerRunning) "Pause" else "Start",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFFB00020), RoundedCornerShape(50)) // Red button
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
