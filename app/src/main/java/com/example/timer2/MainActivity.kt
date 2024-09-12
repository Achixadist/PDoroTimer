package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.timer2.data.AppDatabase
import com.example.timer2.data.ToDoRepository
import com.example.timer2.ui.theme.PomodoroTimerAppTheme
import com.example.timer2.ui.theme.AppNavigation
import com.example.timer2.PreferencesHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database and repository
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pomodoro-timer-db"
        ).build()
        val repository = ToDoRepository(database.toDoItemDao())

        // Initialize the PreferencesHelper
        val preferencesHelper = PreferencesHelper(this)

        setContent {
            MainActivityContent(repository, preferencesHelper)
        }
    }
}

@Composable
fun MainActivityContent(repository: ToDoRepository, preferencesHelper: PreferencesHelper) {
    val navController = rememberNavController()
    val pomodoroTime = remember { mutableStateOf(preferencesHelper.getPomodoroDuration()) } // Load from SharedPreferences
    val isDarkTheme = remember { mutableStateOf(false) }

    PomodoroTimerAppTheme(darkTheme = isDarkTheme.value) {
        AppNavigation(
            navController = navController,
            repository = repository,
            pomodoroTime = pomodoroTime,
            onThemeChange = { isDarkTheme.value = it },
            onPomodoroTimeChange = { newTime ->
                pomodoroTime.value = newTime
                preferencesHelper.savePomodoroDuration(newTime) // Save to SharedPreferences
            }
        )
    }
}
