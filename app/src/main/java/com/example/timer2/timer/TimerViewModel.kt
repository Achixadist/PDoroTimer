package com.example.timer2.timer

import android.app.Application
import android.util.Log
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
    private val _timeLeft = MutableStateFlow(25 * 60 * 1000L) // Initial time in milliseconds
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var finishCallback: (() -> Unit)? = null

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
    private val defaultTimer = SetTimer(TimerSettings().defaultTimer, TimerType.DEFAULT)
    private val workTimer = SetTimer(TimerSettings().currentTimer, TimerType.WORK)
    private val refreshTimer = SetTimer(TimerSettings().refreshTimer, TimerType.REFRESH)
    private val breakTimer = SetTimer(TimerSettings().breakTimer, TimerType.BREAK)

    private var activeTimer: SetTimer = defaultTimer

    fun setPomodoroDuration(minutes: Long) {
        Log.d("TimerViewModel", "Setting Pomodoro Duration to $minutes minutes")
        _timeLeft.value = minutes * 60 * 1000 // Umwandlung von Minuten in Millisekunden
        resetTimer() // Setzt den Timer zurück
    }

    fun onFinishCallback(callback: () -> Unit) {
        finishCallback = callback
    }

    // Function to start the timer with the specified duration
    private fun start(duration: Long, onFinish: () -> Unit) {

        _isRunning.value = true

        viewModelScope.launch {
            val endTime = System.currentTimeMillis() + _timeLeft.value
            while (System.currentTimeMillis() < endTime && _isRunning.value) {
                _timeLeft.value = endTime - System.currentTimeMillis()
                delay(100) // Update every 100ms for smoother countdown
            }
            if (_timeLeft.value <= 0L) {
                _isRunning.value = false
                finishCallback?.invoke() // Call the finish callback here.
                finishCallback?.let { resetFinishCallback() } // Reset callback after use.
            }
        }
    }

    private fun resetFinishCallback() {
        finishCallback=null // Reset the callback to avoid memory leaks.
    }

    private fun stop(){
        _isRunning.value = false
    }

    fun startTimer() {
        start(activeTimer.duration) {
            // Actions when Pomodoro timer finishes
        }
    }

    fun pauseTimer() {
        _isRunning.value = false // Pauses the timer
    }

    fun swapToWork() {
        activeTimer = workTimer
        resetTimer()
    }

    fun swapToRefresh(){
        activeTimer = refreshTimer
        resetTimer()
    }

    fun swapToBreak(){
        activeTimer = breakTimer
        resetTimer()
    }

    fun resetTimer() {
        stop()
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
