package com.example.timer2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timer2.PreferencesHelper

class PomodoroTimerViewModelFactory(
    private val application: Application,
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroTimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PomodoroTimerViewModel(application, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}