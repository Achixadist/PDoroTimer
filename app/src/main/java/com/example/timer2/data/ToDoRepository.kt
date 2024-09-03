package com.example.timer2.data

import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val toDoItemDao: ToDoItemDao) {

    val allToDoItems: Flow<List<ToDoItem>> = toDoItemDao.getAllToDoItems()

    suspend fun insertToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.insertToDoItem(toDoItem)
    }

    suspend fun updateToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.updateToDoItem(toDoItem)
    }

    suspend fun deleteToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.deleteToDoItem(toDoItem)
    }
}
