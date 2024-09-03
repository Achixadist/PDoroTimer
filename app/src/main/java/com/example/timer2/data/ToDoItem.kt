package com.example.timer2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val task: String,
    val isCompleted: Boolean = false
)
