package com.example.simpletodo

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel // NEW import
import com.google.gson.Gson // NEW import
import com.google.gson.reflect.TypeToken // NEW import

// 1. Change to AndroidViewModel and pass in 'app'
class TodoViewModel(app: Application) : AndroidViewModel(app) {

    // --- CONSTANTS ---
    companion object {
        private const val PREFS_NAME = "TodoPrefs"
        private const val TASKS_KEY = "TaskList"
    }

    // --- STATE ---
    val todoList = mutableStateListOf<String>()
    val todoText = mutableStateOf("")

    // --- PERSISTENCE ---
    private val sharedPreferences = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // --- INITIALIZER ---
    init {
        // 3. Load tasks when the ViewModel is created
        loadTasks()
    }

    // --- LOGIC (Events) ---
    fun onTodoTextChange(newText: String) {
        todoText.value = newText
    }

    fun addTask() {
        val text = todoText.value
        if (text.isNotBlank()) {
            todoList.add(text)
            todoText.value = ""
            // 4. Save after adding
            saveTasks()
        }
    }

    fun removeTask(task: String) {
        todoList.remove(task)
        // 5. Save after removing
        saveTasks()
    }

    // --- PRIVATE HELPER FUNCTIONS ---

    // 2. New loadTasks function
    private fun loadTasks() {
        // Get the JSON string from SharedPreferences
        val tasksJson = sharedPreferences.getString(TASKS_KEY, null)
        if (tasksJson != null) {
            // Define the type we want to convert the JSON back to (a List of Strings)
            val type = object : TypeToken<List<String>>() {}.type
            // Convert the JSON string back to a List
            val loadedTasks: List<String> = gson.fromJson(tasksJson, type)
            // Clear the current list and add all the loaded tasks
            todoList.clear()
            todoList.addAll(loadedTasks)
        }
        // If it's null, we just start with an empty list (which it already is)
    }

    // 2. New saveTasks function
    private fun saveTasks() {
        // Convert our todoList to a JSON string
        val tasksJson = gson.toJson(todoList)
        // Save the JSON string to SharedPreferences
        sharedPreferences.edit()
            .putString(TASKS_KEY, tasksJson)
            .apply() // .apply() saves the changes in the background
    }
}