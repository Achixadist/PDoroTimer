package com.example.timer2

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pomodoro_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_POMODORO_DURATION = "key_pomodoro_duration"
        private const val DEFAULT_DURATION = 1500 // 25 minutes in seconds
    }

    fun savePomodoroDuration(duration: Int) {
        prefs.edit().putInt(KEY_POMODORO_DURATION, duration).apply()
    }

    fun getPomodoroDuration(): Int {
        return prefs.getInt(KEY_POMODORO_DURATION, DEFAULT_DURATION)
    }
}
