package com.example.timer2.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.timer2.data.ToDoRepository

@Composable
fun AppNavigation(
    navController: NavHostController,
    repository: ToDoRepository,
    pomodoroTime: MutableState<Int>, // Shared state for Pomodoro time
    onThemeChange: (Boolean) -> Unit,
    onPomodoroTimeChange: (Int) -> Unit // Define the parameter here
) {
    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") {
            ToDoListScreen(navController = navController, repository = repository, pomodoroTime = pomodoroTime)
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onPomodoroTimeChange = onPomodoroTimeChange, // Pass the onPomodoroTimeChange parameter
                onThemeChange = onThemeChange
            )
        }
        composable("pomodoroTimer/{id}") { backStackEntry ->
            PomodoroTimerScreen(
                navController = navController,
                timerDuration = pomodoroTime.value,
                onTimerReset = { /* Logic for resetting the timer */ }
            )
        }
    }
}
