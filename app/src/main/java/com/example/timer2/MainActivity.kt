package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.timer2.ui.theme.PomodoroTimerScreen
import com.example.timer2.ui.theme.PomodoroTimerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerAppTheme {
                PomodoroTimerScreen()
            }
        }
    }
}

