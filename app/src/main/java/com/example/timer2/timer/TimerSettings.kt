package com.example.timer2.timer

data class TimerSettings(
    val defaultTimer: Long = 25 * 60 * 1000, // 25 min
    val currentTimer: Long =  2 * 1000, // * -> check DAO
    val refreshTimer: Long = 5 * 60 * 1000, // 5 min
    val breakTimer: Long = 15 * 60 * 1000 // 5 min
)