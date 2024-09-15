package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer2.data.AppRouter
import com.example.timer2.timer.TimerViewModel
import com.example.timer2.ui.theme.PausenScreen
import com.example.timer2.ui.theme.PomodoroTimerScreen
import com.example.timer2.ui.theme.PomodoroTimerAppTheme
import com.example.timer2.ui.theme.RootNavHost


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerAppTheme {
                RootNavHost()
                    }
                }
            }
        }



