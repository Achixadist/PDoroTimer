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
    BREAKDONE
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

    private val _waiting = MutableLiveData<Boolean>(false)
    val waiting: LiveData<Boolean> get()= _waiting

    private val _navigateToPauseScreen = MutableLiveData<Boolean>(false)
    val navigateToPauseScreen: LiveData<Boolean> = _navigateToPauseScreen

    private val _timerState = MutableLiveData<TimerState>(TimerState.WAITING)
    val timerState: LiveData<TimerState> = _timerState

    private val _currentTimerDuration = MutableLiveData<Long>()
    val currentTimerDuration: LiveData<Long> = _currentTimerDuration

    // ersatz für currenttimer wenn man vom break aus resettet
    private val _currentBreakDuration = MutableLiveData<Long>()
    val currentBreakDuration: LiveData<Long> = _currentBreakDuration

    private val vibrator = application.getSystemService<Vibrator>()

    private var timerJob: Job? = null
    private var initialDuration: Int = 0

    private var mediaPlayer: MediaPlayer? = null

    private val shortBreakDuration = 5 * 60 * 1000L // 5 minutes
    private val longBreakDuration = 15 * 60 * 1000L // 15 minutes
    private var pausedTimeLeft: Long = 0

    fun setInitialDuration(duration: Int) {
        initialDuration = duration
        _timeRemaining.value = duration
        _timeLeft.value = duration * 1000L
        _currentTimerDuration.value = duration * 1000L
        _currentBreakDuration.value = duration * 1000L
        _waiting.value = false
    }

    fun toggleTimer() {
        if (_isRunning.value == true) {
            pauseTimer()
        } else {
            startTimer(pausedTimeLeft.takeIf { it > 0 } ?: _currentTimerDuration.value ?: (initialDuration * 1000L))
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
        if (_isRunning.value == true) return

        _timerState.value = TimerState.RUNNING

        _isRunning.value = true
        _waiting.value = false
        timerJob = viewModelScope.launch {
            val endTime = System.currentTimeMillis() + duration
            while (System.currentTimeMillis() < endTime && _isRunning.value == true) {
                _timeLeft.value = endTime - System.currentTimeMillis()
                _timeRemaining.value = (_timeLeft.value!! / 1000).toInt()
                delay(100) // update Interval
                Log.d("TimerViewModel", "Timer Running")
            }
            if (_timeRemaining.value == 0) {
                _isRunning.value = false
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
                _currentBreakDuration.value = initialDuration * 1000L
            }
            TimerType.SHORT -> {
                _breakRunning.value = true
                _currentBreakDuration.value = shortBreakDuration
            }
            TimerType.LONG -> {
                _breakRunning.value = true
                _currentBreakDuration.value = longBreakDuration
            }
        }
        startTimer(currentBreakDuration.value!!)
    }

    fun endOfTimer() {
        if(_timerType.value == TimerType.NORMAL){
            _timerState.value = TimerState.DONE
        }else{
            _timerState.value = TimerState.BREAKDONE
        }

        _navigateToPauseScreen.value = true
        playAlarmSound()
        vibrate()
        _isRunning.value = false
        _waiting.value = true
        pausedTimeLeft = 0
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
        _isRunning.value = false
        timerJob?.cancel()
        pausedTimeLeft = _timeLeft.value ?: 0
    }

    fun resetTimer() {
        _timerState.value = TimerState.WAITING
        timerJob?.cancel()
        _isRunning.value = false
        _currentTimerDuration.value?.let { duration ->
            _timeLeft.value = duration
            _timeRemaining.value = (duration / 1000).toInt()
        }
        pausedTimeLeft = 0
        _waiting.value = false
        _timerType.value = TimerType.NORMAL
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}