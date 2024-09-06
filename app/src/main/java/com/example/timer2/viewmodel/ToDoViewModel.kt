package com.example.timer2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer2.data.ToDoItem
import com.example.timer2.data.ToDoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToDoViewModel(private val repository: ToDoRepository) : ViewModel() {

    private val _allToDoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    val allToDoItems: StateFlow<List<ToDoItem>> = _allToDoItems.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllToDoItems().collect { items ->
                _allToDoItems.value = items
            }
        }
    }

    fun addToDoItem(name: String) {
        val newItem = ToDoItem(name = name, duration = 25) // Default duration
        viewModelScope.launch {
            repository.insertToDoItem(newItem)
        }
    }

    fun deleteToDoItem(item: ToDoItem) {
        viewModelScope.launch {
            repository.deleteToDoItem(item)
        }
    }
}
