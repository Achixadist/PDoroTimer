package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.timer2.ui.theme.Navigation
import com.example.timer2.ui.theme.PomodoroTimerScreen
import com.example.timer2.ui.theme.PomodoroTimerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerAppTheme {
                Navigation()
            }
        }
    }

    override fun onResume(){
        super.onResume() }
    override fun onPause(){
        super.onPause() }
    override fun onStop(){
        super.onStop() }
    override fun onDestroy(){
        super.onDestroy() }
    override fun onRestart(){
        super.onRestart()
    }
}

