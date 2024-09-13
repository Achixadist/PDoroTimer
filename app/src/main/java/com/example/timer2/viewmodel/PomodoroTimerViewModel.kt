package com.example.timer2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PomodoroTimerViewModel : ViewModel() {

    private val _timeRemaining = MutableLiveData<Int>()
    val timeRemaining: LiveData<Int> = _timeRemaining

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private var timerJob: Job? = null
    private var initialDuration: Int = 0

    fun setInitialDuration(duration: Int) {
        initialDuration = duration
        _timeRemaining.value = duration
    }

    fun toggleTimer() {
        if (_isRunning.value == true) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        if (_isRunning.value == true) return

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timeRemaining.value!! > 0 && _isRunning.value == true) {
                delay(1000)
                _timeRemaining.value = _timeRemaining.value!! - 1
            }
            if (_timeRemaining.value == 0) {
                _isRunning.value = false
            }
        }
    }

    private fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        _timeRemaining.value = initialDuration
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}