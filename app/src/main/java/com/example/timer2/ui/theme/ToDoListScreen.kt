package com.example.timer2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer2.data.ToDoItem
import com.example.timer2.timer.TimerViewModel

@Composable
fun TodoListScreen(navController: NavController, viewModel: TimerViewModel = viewModel()) {
    val todoItems by viewModel.allToDoItems.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("To-Do List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(todoItems) { item ->
                TodoItem(item, viewModel)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Add new todo item */ }) {
            Text("Add New Task")
        }
    }
}

@Composable
fun TodoItem(item: ToDoItem, viewModel: TimerViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = {
                viewModel.updateToDoItem(item.copy(isCompleted = it))
            }
        )
        Text(item.task, modifier = Modifier.weight(1f).padding(start = 8.dp))
        IconButton(onClick = { viewModel.deleteToDoItem(item) }) {
            Text("Delete")
        }
    }
}