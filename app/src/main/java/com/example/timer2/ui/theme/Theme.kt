package com.example.timer2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable

@Composable
fun PomodoroTimerAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        shapes = Shapes(),
        content = content
    )
}
