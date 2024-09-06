package com.example.timer2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_item_table")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val duration: Int
)
