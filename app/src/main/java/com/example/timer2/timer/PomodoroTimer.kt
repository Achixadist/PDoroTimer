package com.example.timer2.timer

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf

class PomodoroTimer(
    private val totalTimeInMillis: Long
) {
    private var timeLeftInMillis = mutableStateOf(totalTimeInMillis)
    private var timerRunning = mutableStateOf(false)
    private var countDownTimer: CountDownTimer? = null

    fun startTimer(onTick: (Long) -> Unit, onFinish: () -> Unit) {
        countDownTimer = object : CountDownTimer(timeLeftInMillis.value, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis.value = millisUntilFinished
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                timerRunning.value = false
                onFinish()
            }
        }.start()

        timerRunning.value = true
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        timerRunning.value = false
    }

    fun resetTimer() {
        countDownTimer?.cancel()
        timeLeftInMillis.value = totalTimeInMillis
        timerRunning.value = false
    }

    fun getTimeLeftInMillis() = timeLeftInMillis.value
    fun isTimerRunning() = timerRunning.value

    // Methods to set timer state from ViewModel
    fun setTimeLeftInMillis(millis: Long) {
        timeLeftInMillis.value = millis
    }

    fun setTimerRunning(running: Boolean) {
        timerRunning.value = running
    }
}
