package com.example.simpletodo // Make sure this matches your package name!

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// The ViewModel class
class TodoViewModel : ViewModel() {

    // --- STATE ---

    // This holds the list of tasks.
    // We use mutableStateListOf so Compose can observe changes to it.
    val todoList = mutableStateListOf<String>()

    // This holds the text the user is typing in the text field.
    // We use mutableStateOf for a single value.
    val todoText = mutableStateOf("")

    // --- INITIALIZER ---

    // This block runs when the ViewModel is first created.
    init {
        // We can add some default data to start
        todoList.add("Buy milk")
        todoList.add("Walk the dog")
        todoList.add("Learn ViewModels")
    }

    // --- LOGIC (or "Events") ---

    // This function is called when the user types in the text field.
    fun onTodoTextChange(newText: String) {
        todoText.value = newText
    }

    // This function adds a new task to the list.
    fun addTask() {
        val text = todoText.value
        if (text.isNotBlank()) {
            todoList.add(text)
            todoText.value = "" // Clear the text field
        }
    }

    // This function removes a task from the list.
    fun removeTask(task: String) {
        todoList.remove(task)
    }
}