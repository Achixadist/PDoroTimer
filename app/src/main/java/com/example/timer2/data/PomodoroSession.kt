package com.example.timer2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_sessions")
data class PomodoroSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duration: Long,
    val timestamp: Long
)
