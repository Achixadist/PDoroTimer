package com.example.timer2.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer2.data.ToDoItem
import com.example.timer2.data.ToDoRepository
import com.example.timer2.viewmodel.ToDoViewModel
import com.example.timer2.viewmodel.ToDoViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListScreen(
    navController: NavController,
    repository: ToDoRepository,
    pomodoroTime: MutableState<Int> // Accept the shared state for Pomodoro time
) {
    val viewModel: ToDoViewModel = viewModel(factory = ToDoViewModelFactory(repository))
    val toDoItems by viewModel.allToDoItems.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var newItemText by remember { mutableStateOf(TextFieldValue("")) }
    var newItemDuration by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(toDoItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.name, modifier = Modifier.weight(1f))
                        Text("Duration: ${item.duration} min") // Display item-specific duration
                        IconButton(onClick = { viewModel.deleteToDoItem(item) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = { navController.navigate("pomodoroTimer/${item.id}") }) {
                            Icon(imageVector = Icons.Default.Timer, contentDescription = "Pomodoro Timer")
                        }
                    }
                }
            }

            // Input for adding new to-do items with improved visibility
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = newItemText,
                    onValueChange = { newItemText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = newItemDuration,
                    onValueChange = { newItemDuration = it },
                    modifier = Modifier
                        .width(50.dp)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (newItemText.text.isNotBlank() && newItemDuration.isNotBlank()) {
                        coroutineScope.launch {
                            viewModel.addToDoItem(
                                ToDoItem(
                                    name = newItemText.text,
                                    duration = newItemDuration.toIntOrNull() ?: 25
                                )
                            )
                            newItemText = TextFieldValue("")
                            newItemDuration = ""
                        }
                    }
                }) {
                    Text("Add New")
                }
            }
        }
    }
}
