package com.example.timer2.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.timer2.PreferencesHelper
import com.example.timer2.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class TimerType {
    NORMAL,
    SHORT,
    LONG
}

class PomodoroTimerViewModel(
    application: Application,
    private val preferencesHelper: PreferencesHelper
) : AndroidViewModel(application) {

    private val _timeRemaining = MutableLiveData<Int>()
    val timeRemaining: LiveData<Int> = _timeRemaining

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private val _timeLeft = MutableLiveData(0L)
    val timeLeft: LiveData<Long> = _timeLeft

    private val _timerType = MutableLiveData<TimerType>(TimerType.NORMAL)
    val timerType: LiveData<TimerType> = _timerType

    private val _breakRunning = MutableLiveData<Boolean>()
    val breakRunning: LiveData<Boolean> = _breakRunning

    private val _waiting = MutableLiveData<Boolean>()
    val waiting: LiveData<Boolean> = _waiting

    private val _navigateToPauseScreen = MutableLiveData<Boolean>(false)
    val navigateToPauseScreen: LiveData<Boolean> = _navigateToPauseScreen

    private val _currentTimerDuration = MutableLiveData<Long>()
    val currentTimerDuration: LiveData<Long> = _currentTimerDuration

    private var timerJob: Job? = null
    private var initialDuration: Int = 0

    private var mediaPlayer: MediaPlayer? = null

    private val shortBreakDuration = 2 * 1000L // 5 minutes
    private val longBreakDuration = 3 * 1000L // 15 minutes
    private var pausedTimeLeft: Long = 0

    private val testDuration = 3 * 1000L // 15 minutes

    fun setInitialDuration(duration: Int) {
        initialDuration = duration
        _timeRemaining.value = duration
        _timeLeft.value = duration * 1000L
        _currentTimerDuration.value = duration * 1000L
        _waiting.value = false
    }

    fun toggleTimer() {
        if (_isRunning.value == true) {
            pauseTimer()
        } else {
            startTimer(pausedTimeLeft.takeIf { it > 0 } ?: _currentTimerDuration.value ?: (initialDuration * 1000L))
        }
    }

    private fun startTimer(duration: Long) {
        if (_isRunning.value == true) return

        _isRunning.value = true
        _waiting.value = false
        timerJob = viewModelScope.launch {
            val endTime = System.currentTimeMillis() + duration
            while (System.currentTimeMillis() < endTime && _isRunning.value == true) {
                _timeLeft.value = endTime - System.currentTimeMillis()
                _timeRemaining.value = (_timeLeft.value!! / 1000).toInt()
                delay(100) // update Interval
                Log.d("TimerViewModel", "Timer Running")
                settingsRead()
            }
            _isRunning.value = false
            if (_timeLeft.value!! <= 0) {
                _timeRemaining.value = 0
                Log.d("TimerViewModel", "Timer Done")
                endOfTimer()
            }
        }
    }

    fun selectTimer(timerType: TimerType) {
        _timerType.value = timerType
        when (timerType) {
            TimerType.NORMAL -> {
                _breakRunning.value = false
                _currentTimerDuration.value = initialDuration * 1000L
            }
            TimerType.SHORT -> {
                _breakRunning.value = true
                _currentTimerDuration.value = shortBreakDuration
            }
            TimerType.LONG -> {
                _breakRunning.value = true
                _currentTimerDuration.value = longBreakDuration
            }
        }
        startTimer(_currentTimerDuration.value!!)
    }

    fun endOfTimer() {
        _navigateToPauseScreen.value = true
        playAlarmSound()
        _waiting.value = true
        pausedTimeLeft = 0
    }

    fun resetPauseScreen() {
        _navigateToPauseScreen.value = false
    }

    fun settingsRead() {
        Log.d("SettingsTest", "${preferencesHelper.getSoundEnabled()}")
    }

    private fun playAlarmSound() {
        if (preferencesHelper.getSoundEnabled()) {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.alarm_sound)
            mediaPlayer?.setOnErrorListener { mp, what, extra ->
                Log.e("MediaPlayer", "Error occurred: what=$what, extra=$extra")
                true
            }

            mediaPlayer?.start()

            viewModelScope.launch {
                mediaPlayer?.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
            }
        }
    }

    private fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        pausedTimeLeft = _timeLeft.value ?: 0
    }

    fun resetTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        _currentTimerDuration.value?.let { duration ->
            _timeLeft.value = duration
            _timeRemaining.value = (duration / 1000).toInt()
        }
        pausedTimeLeft = 0
        _waiting.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}