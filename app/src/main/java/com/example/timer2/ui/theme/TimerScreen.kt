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
import androidx.navigation.NavController
import com.example.timer2.timer.TimerType
import com.example.timer2.timer.TimerViewModel

@Composable
fun PomodoroTimerScreen(
    navController: NavController,
    viewModel: TimerViewModel = viewModel()
) {
    // Observing state from ViewModel
    val timeLeftInMillis by viewModel.timeLeft.collectAsState()
    val isTimerRunning by viewModel.isRunning.collectAsState()

    val minutes = (timeLeftInMillis / 1000) / 60
    val seconds = (timeLeftInMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    val fullMinutes = (viewModel.activeTimer.duration / 1000) / 60
    val fullSeconds = (viewModel.activeTimer.duration / 1000) % 60
    val fullTimerFormatted = String.format("%02d:%02d", fullMinutes, fullSeconds)

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
                modifier = Modifier.padding(0.dp)
            )


            Text(
                text = if (isTimerRunning) "${viewModel.activeTimer.timerName + " " + fullTimerFormatted}"
                else viewModel.activeTimer.timerName,
                fontSize = 30.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(0.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isTimerRunning) viewModel.pauseTimer()
                    else viewModel.startTimer() // Calls the start method for Pomodoro
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF6200EE), RoundedCornerShape(50))
            ) {
                Text(
                    text = if (isTimerRunning) "Pause Timer" else "Start Timer",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.swapTo(TimerType.WORK) // Calls the start method for Short Break
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF03DAC5), RoundedCornerShape(50))
            ) {
                Text(
                    text = "Swap to List-Work",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.swapTo(TimerType.REFRESH) // Calls the start method for Short Break
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF03DAC5), RoundedCornerShape(50))
            ) {
                Text(
                    text = "Swap to Refresh",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.swapTo(TimerType.BREAK) // Calls the start method for Long Break
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF018786), RoundedCornerShape(50))
            ) {
                Text(
                    text = "Swap to Break",
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


            //NAVIGATION
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.navigate(Screen.TodoList.route) }) {
                    Text("To-Do")
                }
                Button(onClick = { navController.navigate(Screen.Tutorial.route) }) {
                    Text("Tutorial")
                }
                Button(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Text("Settings")
                }
            }

        }
    }
}
