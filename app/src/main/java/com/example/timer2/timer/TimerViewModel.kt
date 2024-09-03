package com.example.timer2.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer2.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PomodoroRepository
    val allSessions: Flow<List<PomodoroSession>>

    private val toDoRepository: ToDoRepository
    val allToDoItems: Flow<List<ToDoItem>>

    // StateFlows to handle the timer's time and running state
    private val _timeLeft = MutableStateFlow(25 * 60 * 1000L) // Initial time for Pomodoro in milliseconds
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    init {
        val database = PomodoroDatabase.getDatabase(application)
        val sessionDao = database.pomodoroSessionDao()
        repository = PomodoroRepository(sessionDao)
        allSessions = repository.allSessions

        val toDoDao = database.toDoItemDao()
        toDoRepository = ToDoRepository(toDoDao)
        allToDoItems = toDoRepository.allToDoItems
    }

    // Timer durations in milliseconds
    private val pomodoroDuration = 25 * 60 * 1000L
    private val shortBreakDuration = 5 * 60 * 1000L
    private val longBreakDuration = 15 * 60 * 1000L

    // Function to start the timer with the specified duration
    private fun startTimer(duration: Long, onFinish: () -> Unit) {
        _isRunning.value = true
        _timeLeft.value = duration

        viewModelScope.launch {
            while (_timeLeft.value > 0 && _isRunning.value) {
                delay(1000) // Wait for 1 second
                _timeLeft.value -= 1000 // Decrease the time left by 1 second
            }
            if (_timeLeft.value <= 0) {
                _isRunning.value = false
                onFinish()
            }
        }
    }

    fun startPomodoroTimer() {
        startTimer(pomodoroDuration) {
            // Actions when Pomodoro timer finishes
        }
    }

    fun startShortBreak() {
        startTimer(shortBreakDuration) {
            // Actions when short break finishes
        }
    }

    fun startLongBreak() {
        startTimer(longBreakDuration) {
            // Actions when long break finishes
        }
    }

    fun pauseTimer() {
        _isRunning.value = false // Pauses the timer
    }

    fun resetTimer() {
        _isRunning.value = false // Stops the timer
        _timeLeft.value = pomodoroDuration // Resets the timer to the Pomodoro duration
    }

    // To-Do Methods
    fun insertToDoItem(toDoItem: ToDoItem) {
        viewModelScope.launch {
            toDoRepository.insertToDoItem(toDoItem)
        }
    }

    fun updateToDoItem(toDoItem: ToDoItem) {
        viewModelScope.launch {
            toDoRepository.updateToDoItem(toDoItem)
        }
    }

    fun deleteToDoItem(toDoItem: ToDoItem) {
        viewModelScope.launch {
            toDoRepository.deleteToDoItem(toDoItem)
        }
    }
}
