package com.example.timer2.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.content.getSystemService
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

enum class TimerState {
    WAITING,
    RUNNING,
    PAUSED,
    DONE,
    BREAK
}

class PomodoroTimerViewModel(
    application: Application,
    private val preferencesHelper: PreferencesHelper
) : AndroidViewModel(application) {

    private val _timerState = MutableLiveData<TimerState>(TimerState.WAITING)
    val timerState: LiveData<TimerState> = _timerState

    private val _timeLeft = MutableLiveData(0L)
    val timeLeft: LiveData<Long> = _timeLeft

    private val _timerType = MutableLiveData<TimerType>(TimerType.NORMAL)
    val timerType: LiveData<TimerType> = _timerType

    private val _navigateToPauseScreen = MutableLiveData<Boolean>(false)
    val navigateToPauseScreen: LiveData<Boolean> = _navigateToPauseScreen

    private val _currentTimerDuration = MutableLiveData<Long>()
    val currentTimerDuration: LiveData<Long> = _currentTimerDuration

    private var initialDuration: Long = 0L
    private val vibrator = application.getSystemService<Vibrator>()
    private var timerJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    private val shortBreakDuration = 5 * 60 * 1000L // 5 minutes
    private val longBreakDuration = 15 * 60 * 1000L // 15 minutes

    fun setInitialDuration(duration: Int) {
        initialDuration = duration * 1000L
        _currentTimerDuration.value = initialDuration
        _timeLeft.value = initialDuration
        _timerState.value = TimerState.WAITING
    }

    fun toggleTimer() {
        when (_timerState.value) {
            TimerState.RUNNING -> pauseTimer()
            TimerState.PAUSED, TimerState.WAITING -> startTimer(_timeLeft.value ?: _currentTimerDuration.value ?: 0L)
            else -> {} // Do nothing for DONE and BREAKDONE states
        }
    }

    private fun vibrate() {
        val isVibrationEnabled = preferencesHelper.getVibrationEnabled()
        Log.d("PomodoroTimer", "Vibration enabled: $isVibrationEnabled")
        if (preferencesHelper.getVibrationEnabled() && vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(1000)
            }
        }
    }

    private fun startTimer(duration: Long) {
        if (_timerState.value == TimerState.RUNNING) return

        _timerState.value = TimerState.RUNNING
        timerJob = viewModelScope.launch {
            val endTime = System.currentTimeMillis() + duration
            while (System.currentTimeMillis() < endTime && _timerState.value == TimerState.RUNNING) {
                _timeLeft.value = endTime - System.currentTimeMillis()
                delay(100) // update interval
            }
            endOfTimer()
        }
    }

    fun selectTimer(timerType: TimerType) {
        _timerType.value = timerType
        _currentTimerDuration.value = when (timerType) {
            TimerType.NORMAL -> initialDuration
            TimerType.SHORT -> shortBreakDuration
            TimerType.LONG -> longBreakDuration
        }
        _timeLeft.value = _currentTimerDuration.value
        _timerState.value = TimerState.WAITING
    }

    private fun endOfTimer() {
        _timerState.value = if (_timerType.value == TimerType.NORMAL) TimerState.DONE else TimerState.BREAK
        _navigateToPauseScreen.value = true
        playAlarmSound()
        vibrate()
    }

    fun resetPauseScreen() {
        _navigateToPauseScreen.value = false
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
        _timerState.value = TimerState.PAUSED
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.WAITING
        _timerType.value = TimerType.NORMAL
        _currentTimerDuration.value = initialDuration
        _timeLeft.value = initialDuration
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}