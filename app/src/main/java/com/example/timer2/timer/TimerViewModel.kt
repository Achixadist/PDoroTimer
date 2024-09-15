package com.example.timer2.timer

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer2.R
import com.example.timer2.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private var _navigateToPauseScreen = MutableStateFlow(false)
    val navigateToPauseScreen: StateFlow<Boolean> get() = _navigateToPauseScreen

    private var _navigateToStartScreen = MutableStateFlow(false)
    val navigateToStartScreen: StateFlow<Boolean> get() = _navigateToStartScreen

    private val repository: PomodoroRepository
    val allSessions: Flow<List<PomodoroSession>>

    private val toDoRepository: ToDoRepository
    val allToDoItems: Flow<List<ToDoItem>>

    // StateFlows to handle the timer's time and running state
    private val _timeLeft = MutableStateFlow(1 * 3 * 1000L) // Initial time in milliseconds
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get()= _isRunning

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

    // Alarm
    private var mediaPlayer: MediaPlayer? = null

    private fun playAlarmSound() {
        // Initialisiere den MediaPlayer mit der Alarm-Sounddatei
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.alarm_sound)
        // media player fehlerbehandlung
        mediaPlayer?.setOnErrorListener { mp, what, extra ->
            Log.e("MediaPlayer", "Error occurred: what=$what, extra=$extra")
            true // Fehler wurde behandelt
        }

        mediaPlayer?.start()

        viewModelScope.launch {
            mediaPlayer = null // Setze auf null, um Speicherlecks zu vermeiden
        }
    }

    // Setting Timers
    private val defaultTimer = SetTimer(TimerSettings().defaultTimer, TimerType.DEFAULT)
    private val workTimer = SetTimer(TimerSettings().currentTimer, TimerType.WORK)
    private val refreshTimer = SetTimer(TimerSettings().refreshTimer, TimerType.REFRESH)
    private val breakTimer = SetTimer(TimerSettings().breakTimer, TimerType.BREAK)

    private var activeTimer: SetTimer = defaultTimer

    private fun resetFinishCallback() {
        finishCallback = null // Reset the callback to avoid memory leaks.
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
                //if (_timeLeft.value <= 0L) {
                _isRunning.value = false
                //finishCallback?.invoke() // Call the finish callback here.
                //finishCallback?.let { resetFinishCallback() } // Reset callback after use.*/
                endOfTimer()
            }
        }


    fun resetNavigation() {
        _navigateToPauseScreen.value = false // Setze den Wert zurück
    }


    fun startTimer() {
        start(activeTimer.duration) {
            // Actions when Pomodoro timer finishes
            /* if (_timeLeft.value <= 0L) {
                _isRunning.value = false
                finishCallback?.invoke() // Call the finish callback here.
                resetFinishCallback() // Reset callback after use.*/
        }
    }
    //}

    fun endOfTimer() {
        _navigateToPauseScreen.value = true // Setze den Wert auf true, wenn der Timer abläuft
        playAlarmSound()

    }


    fun pauseTimer() {
        _isRunning.value = false // Pauses the timer
    }

    fun swapToWork() {
        activeTimer = workTimer
        resetTimer()
    }

    fun swapToRefresh() {
        activeTimer = refreshTimer
        start(activeTimer.duration){
        }
        resetTimer()
    }

    fun swapToBreak() {
        activeTimer = breakTimer
        resetTimer()
    }

    fun resetTimer() {
        pauseTimer()
        _timeLeft.value = activeTimer.duration
    }

    /*fun startBreakTimer() {
        activeTimer = breakTimer
        resetTimer()
        startTimer()
        }


    fun startRefreshTimer() {
        activeTimer = refreshTimer
        resetTimer()
        startTimer()
    }*/
}









   /* // To-Do Methods
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
*/