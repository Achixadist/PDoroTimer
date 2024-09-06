package com.example.timer2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {

    @Query("SELECT * FROM to_do_item_table")
    fun getAllToDoItems(): Flow<List<ToDoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoItem(item: ToDoItem)

    @Delete
    suspend fun deleteToDoItem(item: ToDoItem)
}
