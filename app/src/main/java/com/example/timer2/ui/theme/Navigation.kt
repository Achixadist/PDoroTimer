package com.example.timer2.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Timer : Screen("timer")
    object TodoList : Screen("list")
    object Tutorial : Screen("tutorial")
    object Settings : Screen("settings")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Timer.route) {
        composable(Screen.Timer.route) {
            PomodoroTimerScreen(navController)
        }
        composable(Screen.TodoList.route) {
            TodoListScreen(navController)
        }
        composable(Screen.Tutorial.route) {
            TutorialScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}