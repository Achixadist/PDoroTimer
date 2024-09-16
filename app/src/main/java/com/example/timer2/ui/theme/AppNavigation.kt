package com.example.timer2.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.timer2.PreferencesHelper
import com.example.timer2.data.ToDoRepository
import com.example.timer2.viewmodel.SettingsViewModel
import com.example.timer2.viewmodel.ToDoViewModel
import com.example.timer2.viewmodel.ToDoViewModelFactory

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
    isDarkTheme: MutableState<Boolean>,
    preferencesHelper: PreferencesHelper
) {
    val toDoViewModel: ToDoViewModel = viewModel(factory = ToDoViewModelFactory(repository))

    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") {
            ToDoListScreen(
                navController = navController,
                repository = repository,
                pomodoroTime = pomodoroTime
            )
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
                isDarkTheme = isDarkTheme.value
            )
        }
        composable("Pause"){
            PauseScreen(
                navController = navController,
                onPomodoroTimeChange = onPomodoroTimeChange,
                onThemeChange = onThemeChange,
                onSoundChange = onSoundChange,
                onVibrationChange = onVibrationChange,
                isSoundEnabled = isSoundEnabled,
                isVibrationEnabled = isVibrationEnabled,
                isDarkTheme = isDarkTheme.value
            )
        }
        composable(
            "pomodoroTimer/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("id") ?: 0
            val toDoItem = toDoViewModel.getToDoItemById(itemId).collectAsState(initial = null).value

            if (toDoItem != null) {
                PomodoroTimerScreen(
                    navController = navController,
                    timerDuration = toDoItem.duration  , // Change for testing !!!!!!!!!!
                    onTimerReset = { /* Logic for resetting the timer */ },
                    preferencesHelper = preferencesHelper,
                    selectedTimerName = toDoItem.name
                )
            } else {
                // Handle case when item is not found (e.g., show an error message or navigate back)
            }
        }
    }
}