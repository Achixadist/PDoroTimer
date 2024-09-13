package com.example.timer2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _pomodoroTime = MutableLiveData<Int>(25 * 60) // Default 25 minutes in seconds
    val pomodoroTime: LiveData<Int> = _pomodoroTime

    private val _isDarkMode = MutableLiveData<Boolean>(false)
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    private val _isSoundEnabled = MutableLiveData<Boolean>(true)
    val isSoundEnabled: LiveData<Boolean> = _isSoundEnabled

    private val _isVibrationEnabled = MutableLiveData<Boolean>(true)
    val isVibrationEnabled: LiveData<Boolean> = _isVibrationEnabled

    private val _showInputDialog = MutableLiveData<Boolean>(false)
    val showInputDialog: LiveData<Boolean> = _showInputDialog

    fun setPomodoroTime(minutes: Int) {
        _pomodoroTime.value = minutes * 60 // Convert minutes to seconds
    }

    fun setDarkMode(isDark: Boolean) {
        _isDarkMode.value = isDark
    }

    fun setSoundEnabled(isEnabled: Boolean) {
        _isSoundEnabled.value = isEnabled
    }

    fun setVibrationEnabled(isEnabled: Boolean) {
        _isVibrationEnabled.value = isEnabled
    }

    fun toggleInputDialog(show: Boolean) {
        _showInputDialog.value = show
    }
}