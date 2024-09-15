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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.timer2.viewmodel.SettingsViewModel
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
    val timeRemaining by viewModel.timeRemaining.observeAsState(timerDuration)
    val isRunning by viewModel.isRunning.observeAsState(false)
    val waiting by viewModel.waiting.observeAsState(false)
    val timerType by viewModel.timerType.observeAsState(TimerType.NORMAL)

    LaunchedEffect(timerDuration) {
        viewModel.setInitialDuration(timerDuration)
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
            if (waiting){
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

            Text(
                text = "Time: ${timeRemaining / 60}:${(timeRemaining % 60).toString().padStart(2, '0')}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.toggleTimer() }) {
                    Text(if (isRunning) "Pause" else "Start")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.resetTimer()
                    onTimerReset()
                }) {
                    Text("Reset Timer")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigateUp() }) {
                Text("Return")
            }
        }
    }
}