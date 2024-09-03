package com.example.timer2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoItem(toDoItem: ToDoItem)

    @Update
    suspend fun updateToDoItem(toDoItem: ToDoItem)

    @Delete
    suspend fun deleteToDoItem(toDoItem: ToDoItem)

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    fun getAllToDoItems(): Flow<List<ToDoItem>>
}
