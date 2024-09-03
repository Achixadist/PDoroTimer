package com.example.timer2.timer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer")
data class TimerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timeLeftInMillis: Long,
    val isRunning: Boolean
)
