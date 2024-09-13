package com.example.timer2.timer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer2.data.*
import kotlinx.coroutines.Job
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
    private val _timeLeft = MutableStateFlow(25 * 60 * 1000L) // Initial time in milliseconds
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _currentTimerType = MutableStateFlow(TimerType.DEFAULT)
    val currentTimerType: StateFlow<TimerType> = _currentTimerType

    private val _timerTime = MutableStateFlow(25 * 60 * 1000L)
    val timerTime: StateFlow<Long> = _timerTime

    private var timerJob: Job? = null

    init {
        val database = PomodoroDatabase.getDatabase(application)
        val sessionDao = database.pomodoroSessionDao()
        repository = PomodoroRepository(sessionDao)
        allSessions = repository.allSessions

        val toDoDao = database.toDoItemDao()
        toDoRepository = ToDoRepository(toDoDao)
        allToDoItems = toDoRepository.allToDoItems
    }

    // Setting Timers
    private val defaultTimer = SetTimer(TimerSettings().defaultTimer, TimerType.DEFAULT, "Default")
    private val workTimer = SetTimer(TimerSettings().currentTimer, TimerType.WORK, "Work")
    private val refreshTimer = SetTimer(TimerSettings().refreshTimer, TimerType.REFRESH, "Refresh")
    private val breakTimer = SetTimer(TimerSettings().breakTimer, TimerType.BREAK, "Break")

    var activeTimer: SetTimer = defaultTimer

    // Function to start the timer with the specified duration
    private fun start(duration: Long, onFinish: () -> Unit) {

        _isRunning.value = true

        timerJob = viewModelScope.launch {
            val endTime = System.currentTimeMillis() + _timeLeft.value
            while (System.currentTimeMillis() < endTime && _isRunning.value) {
                _timeLeft.value = endTime - System.currentTimeMillis()
                delay(100) // Update every 100ms for smoother countdown
                Log.d("TimerViewModel", "Test1")
            }
            if (_timeLeft.value <= 0L) {
                Log.d("TimerViewModel", "Test2")
                _isRunning.value = false
                onFinish()
            }
        }
    }

    fun startTimer() {
        start(activeTimer.duration) {
            Log.d("TimerViewModel", "Test3")
        }
    }

    fun pauseTimer() {
        _isRunning.value = false // Pauses the timer
        timerJob?.cancel()
    }

    fun swapTo(timerType: TimerType){
        activeTimer = when (timerType){
            TimerType.DEFAULT -> defaultTimer
            TimerType.WORK -> workTimer
            TimerType.REFRESH -> refreshTimer
            TimerType.BREAK -> breakTimer
        }
        resetTimer()
        _timerTime.value = activeTimer.duration
    }

    fun resetTimer() {
        pauseTimer()
        _timeLeft.value = activeTimer.duration
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
