package com.example.timer2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.timer2.data.AppDatabase
import com.example.timer2.data.ToDoRepository
import com.example.timer2.ui.theme.ToDoListScreen
import com.example.timer2.ui.theme.PomodoroTimerScreen
import com.example.timer2.ui.theme.PomodoroTimerAppTheme
import com.example.timer2.viewmodel.ToDoViewModel
import com.example.timer2.viewmodel.ToDoViewModelFactory

class MainActivity : ComponentActivity() {

    // Initialize the database and repository
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "pomodoro-timer-db"
        ).fallbackToDestructiveMigration().build()
    }

    private val toDoRepository by lazy { ToDoRepository(database.toDoItemDao()) }

    // Use the factory to provide the repository to the ToDoViewModel
    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory(toDoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(toDoViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(toDoViewModel: ToDoViewModel) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") }
            )
        },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "todo_list"
            ) {
                composable("todo_list") {
                    ToDoListScreen(
                        viewModel = toDoViewModel,
                        onPomodoroStart = { taskName ->
                            navController.navigate("pomodoro_timer/$taskName")
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                composable("pomodoro_timer/{taskName}") { backStackEntry ->
                    val taskName = backStackEntry.arguments?.getString("taskName") ?: ""
                    PomodoroTimerScreen(
                        taskName = taskName,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    )
}
