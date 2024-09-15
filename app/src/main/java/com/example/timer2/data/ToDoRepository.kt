package com.example.timer2.data

import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val toDoItemDao: ToDoItemDao) {

    fun getAllToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getAllToDoItems()
    }

    suspend fun insertToDoItem(item: ToDoItem) {
        toDoItemDao.insertToDoItem(item)
    }

    fun getToDoItemById(id: Int): Flow<ToDoItem?> {
        return toDoItemDao.getToDoItemById(id)
    }

    suspend fun deleteToDoItem(item: ToDoItem) {
        toDoItemDao.deleteToDoItem(item)
    }
}
