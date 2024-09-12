package com.example.timer2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer2.data.ToDoItem
import com.example.timer2.data.ToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ToDoViewModel(private val repository: ToDoRepository) : ViewModel() {

    val allToDoItems: Flow<List<ToDoItem>> = repository.getAllToDoItems()

    fun addToDoItem(toDoItem: ToDoItem) {
        viewModelScope.launch {
            repository.insertToDoItem(toDoItem)
        }
    }

    fun deleteToDoItem(toDoItem: ToDoItem) {
        viewModelScope.launch {
            repository.deleteToDoItem(toDoItem)
        }
    }
}
