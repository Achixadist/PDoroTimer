package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.timer2.data.AppDatabase
import com.example.timer2.data.ToDoRepository
import com.example.timer2.ui.theme.AppNavigation
import com.example.timer2.ui.theme.PomodoroTimerAppTheme

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
    val pomodoroTime = remember { mutableStateOf(preferencesHelper.getPomodoroDuration()) }
    val isDarkTheme = remember { mutableStateOf(preferencesHelper.getDarkThemeEnabled()) }
    val isSoundEnabled = remember { mutableStateOf(preferencesHelper.getSoundEnabled()) }
    val isVibrationEnabled = remember { mutableStateOf(preferencesHelper.getVibrationEnabled()) }

    PomodoroTimerAppTheme(darkTheme = isDarkTheme.value) {
        AppNavigation(
            navController = navController,
            repository = repository,
            pomodoroTime = pomodoroTime,
            onThemeChange = { newValue ->
                isDarkTheme.value = newValue
                preferencesHelper.saveDarkThemeEnabled(newValue)
            },
            onPomodoroTimeChange = { newTime ->
                pomodoroTime.value = newTime
                preferencesHelper.savePomodoroDuration(newTime)
            },
            onSoundChange = { isEnabled ->
                isSoundEnabled.value = isEnabled
                preferencesHelper.saveSoundEnabled(isEnabled)
            },
            onVibrationChange = { isEnabled ->
                isVibrationEnabled.value = isEnabled
                preferencesHelper.saveVibrationEnabled(isEnabled)
            },
            isSoundEnabled = isSoundEnabled,
            isVibrationEnabled = isVibrationEnabled,
            isDarkTheme = isDarkTheme, // Add this line to pass isDarkTheme
            preferencesHelper = preferencesHelper
        )
    }
}