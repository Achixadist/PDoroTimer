package com.example.timer2.ui.theme

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer2.PreferencesHelper
import com.example.timer2.viewmodel.PomodoroTimerViewModel
import com.example.timer2.viewmodel.PomodoroTimerViewModelFactory
import com.example.timer2.viewmodel.TimerState
import com.example.timer2.viewmodel.TimerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTimerScreen(
    navController: NavController,
    timerDuration: Int,
    onTimerReset: () -> Unit,
    preferencesHelper: PreferencesHelper,
    viewModel: PomodoroTimerViewModel = viewModel(
        factory = PomodoroTimerViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            preferencesHelper = preferencesHelper
        )
    ),
    selectedTimerName: String
) {
    val timeLeft by viewModel.timeLeft.observeAsState(timerDuration * 1000L)
    val timerType by viewModel.timerType.observeAsState(TimerType.NORMAL)
    val timerState by viewModel.timerState.observeAsState(TimerState.WAITING)

    LaunchedEffect(timerDuration) {
        viewModel.setInitialDuration(timerDuration)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetTimer() // This will stop the foreground service when leaving the screen
        }
    }

    Scaffold(
        // ... (keep existing top bar)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (timerState == TimerState.DONE || timerState == TimerState.BREAK){
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { viewModel.selectTimer(TimerType.NORMAL) }) {
                        Text("Continue Working")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.selectTimer(TimerType.SHORT) }) {
                        Text("Short Break (5:00)")
                    }
                }
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.selectTimer(TimerType.LONG) }) {
                        Text("Long Break (15:00)")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = when (timerType) {
                    TimerType.NORMAL -> selectedTimerName
                    TimerType.SHORT -> "Short Break"
                    TimerType.LONG -> "Long Break"
                },
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            val minutes = (timeLeft / 1000 / 60).toInt()
            val seconds = ((timeLeft / 1000) % 60).toInt()
            Text(
                text = "${timerState.name}: $minutes:${seconds.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if(timerState != TimerState.BREAK && timerState != TimerState.DONE){
                    Button(onClick = { viewModel.toggleTimer() }) {
                        Text(if (timerState == TimerState.RUNNING) "Pause" else "Start")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.resetTimer()
                        onTimerReset()
                    }) {
                        Text("Reset Timer")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigateUp() }) {
                Text("Return")
            }
        }
    }
}