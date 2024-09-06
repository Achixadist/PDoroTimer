package com.example.timer2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_session_table")
data class PomodoroSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duration: Int,
    val type: String,  // Example: "Pomodoro", "Short Break", "Long Break"
    val timestamp: Long
)
