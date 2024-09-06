package com.example.timer2.data

import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val toDoItemDao: ToDoItemDao) {

    fun getAllToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getAllToDoItems()
    }

    suspend fun insertToDoItem(item: ToDoItem) {
        toDoItemDao.insertToDoItem(item)
    }

    suspend fun deleteToDoItem(item: ToDoItem) {
        toDoItemDao.deleteToDoItem(item)
    }
}
