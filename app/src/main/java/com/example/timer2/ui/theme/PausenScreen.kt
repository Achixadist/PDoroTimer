package com.example.timer2.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.timer2.timer.TimerViewModel


@Composable
fun PausenScreen(navController: NavHostController, viewModel: TimerViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Time`s Up!" +
                "What´s next?", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Starte den Refresh-Timer und navigiere zurück zum Hauptbildschirm
            viewModel.swapToRefresh()
            navController.navigate("Start")
        }) {
            Text(text = "Refresh (5 Minuten)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // Starte den Break-Timer und navigiere zurück zum Hauptbildschirm
            viewModel.swapToBreak()
            navController.navigate("Start")
        }) {
            Text(text = "Break (15 Minuten)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // Hier kannst du später die Funktionalität für Presets hinzufügen.
            // Aktuell navigieren wir einfach zurück zum Hauptbildschirm.
            navController.navigate("TimerScreen")
        }) {
            Text(text = "Presets")
        }
    }
}



