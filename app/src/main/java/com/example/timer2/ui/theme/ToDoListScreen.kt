package com.example.timer2.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timer2.data.ToDoItem
import com.example.timer2.viewmodel.ToDoViewModel
import java.util.concurrent.TimeUnit

@Composable
fun ToDoListScreen(
    viewModel: ToDoViewModel = viewModel(),
    onPomodoroStart: (String) -> Unit, // Callback to navigate to Pomodoro Timer
    modifier: Modifier = Modifier
) {
    val toDoItems by viewModel.allToDoItems.collectAsState(initial = emptyList())
    val totalPomodoroTime = 0L // Placeholder for total Pomodoro time

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display each to-do item with an option to start Pomodoro timer
        toDoItems.forEach { item ->
            ToDoItemRow(
                item = item,
                onPomodoroStart = { onPomodoroStart(item.name) }, // Trigger navigation to timer
                onDelete = { viewModel.deleteToDoItem(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add new to-do item section
        var newTaskName by remember { mutableStateOf(TextFieldValue("")) }

        // Input field to enter task name
        BasicTextField(
            value = newTaskName,
            onValueChange = { newTaskName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (newTaskName.text.isEmpty()) {
                    Text("Enter task name", style = MaterialTheme.typography.bodyMedium)
                }
                innerTextField()
            }
        )

        // Button to add new task
        Button(onClick = {
            if (newTaskName.text.isNotBlank()) {
                viewModel.addToDoItem(newTaskName.text) // Add new task to the list
                newTaskName = TextFieldValue("") // Clear the input field
            }
        }) {
            Text(text = "Add New")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Total Pomodoro Time at the bottom
        val totalMinutes = TimeUnit.MILLISECONDS.toMinutes(totalPomodoroTime) % 60
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(totalPomodoroTime) % 60
        Text(
            text = "Total Pomodoro Time: ${String.format("%02d:%02d", totalMinutes, totalSeconds)}",
            fontSize = 18.sp
        )
    }
}

@Composable
fun ToDoItemRow(item: ToDoItem, onPomodoroStart: () -> Unit, onDelete: (ToDoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onPomodoroStart() }, // Click to navigate to the Pomodoro timer
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name)
        Row {
            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
