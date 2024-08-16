package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.timer2.ui.theme.Timer2Theme
import com.example.timer2.ui.theme.PomodoroTimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Timer2Theme {
                PomodoroTimerScreen()
            }
        }
    }
}