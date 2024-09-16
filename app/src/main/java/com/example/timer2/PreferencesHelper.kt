package com.example.timer2

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("PomodoroPrefs", Context.MODE_PRIVATE)
    fun getPomodoroDuration(): Int = prefs.getInt("pomodoro_duration", 25 * 60) // Default 25 minutes
    fun savePomodoroDuration(duration: Int) = prefs.edit().putInt("pomodoro_duration", duration).apply()
    fun getDarkThemeEnabled(): Boolean = prefs.getBoolean("dark_theme_enabled", false)
    fun saveDarkThemeEnabled(enabled: Boolean) = prefs.edit().putBoolean("dark_theme_enabled", enabled).apply()
    fun getSoundEnabled(): Boolean = prefs.getBoolean("sound_enabled", true)
    fun saveSoundEnabled(enabled: Boolean) = prefs.edit().putBoolean("sound_enabled", enabled).apply()
    fun getVibrationEnabled(): Boolean = prefs.getBoolean("vibration_enabled", true)
    fun saveVibrationEnabled(enabled: Boolean) = prefs.edit().putBoolean("vibration_enabled", enabled).apply()
}
