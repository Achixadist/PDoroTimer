package com.example.timer2.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val _timeLeft = MutableStateFlow(25 * 60 * 1000L) // 25 minutes in milliseconds
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var timerJob: Job? = null

    fun startTimer(onTick: () -> Unit = {}, onFinish: () -> Unit = {}) {
        if (_isRunning.value) return

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1000L
                onTick()
            }
            onFinish()
            _isRunning.value = false
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        _timeLeft.value = 25 * 60 * 1000L // Reset to 25 minutes
    }
}
