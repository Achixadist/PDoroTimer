package com.example.timer2.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer2.timer.TimerViewModel

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    val timerViewModel: TimerViewModel = viewModel()
// Definiere die NavHost mit den Composables für jede Route
    val navigateToPauseScreen by timerViewModel.navigateToPauseScreen.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "Start"
    ) {
        composable("Start") {
            PomodoroTimerScreen(
                viewModel = viewModel<TimerViewModel>(),
                navController = navController
            )
        }
        composable("Pause") {
            PausenScreen(
                navController = navController,
                viewModel = viewModel<TimerViewModel>()
            )
        }
    }
    /*LaunchedEffect(navigateToPauseScreen) {
        if (navigateToPauseScreen) {
            navController.navigate("Pause")
        }
    }*/
}

