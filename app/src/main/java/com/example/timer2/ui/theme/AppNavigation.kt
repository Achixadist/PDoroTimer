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
    pomodoroTime: MutableState<Int>,
    onThemeChange: (Boolean) -> Unit,
    onPomodoroTimeChange: (Int) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    isSoundEnabled: MutableState<Boolean>,
    isVibrationEnabled: MutableState<Boolean>,
    isDarkTheme: MutableState<Boolean> // Add this new parameter
) {
    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") {
            ToDoListScreen(navController = navController, repository = repository, pomodoroTime = pomodoroTime)
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onPomodoroTimeChange = onPomodoroTimeChange,
                onThemeChange = onThemeChange,
                onSoundChange = onSoundChange,
                onVibrationChange = onVibrationChange,
                isSoundEnabled = isSoundEnabled,
                isVibrationEnabled = isVibrationEnabled,
                isDarkTheme = isDarkTheme.value // Pass the current theme state here
            )
        }
        composable("pomodoroTimer/{id}") { backStackEntry ->
            PomodoroTimerScreen(
                navController = navController,
                timerDuration = pomodoroTime.value,
                onTimerReset = { /* Logic for resetting the timer */ },
                isSoundEnabled = isSoundEnabled.value,
                isVibrationEnabled = isVibrationEnabled.value
            )
        }
    }
}