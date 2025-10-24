package com.example.simpletodo

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// 1. CHANGE: We now use TodoItem, not String
class TodoViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private const val PREFS_NAME = "TodoPrefs"
        private const val TASKS_KEY = "TaskList"
    }

    // --- STATE ---
    // 2. CHANGE: The list is now of type TodoItem
    val todoList = mutableStateListOf<TodoItem>()
    val todoText = mutableStateOf("")

    private val sharedPreferences = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // 3. NEW: A counter to create unique IDs
    private var lastId = 0L

    init {
        loadTasks()
    }

    // --- LOGIC (Events) ---
    fun onTodoTextChange(newText: String) {
        todoText.value = newText
    }

    fun addTask() {
        val text = todoText.value
        if (text.isNotBlank()) {
            // 4. CHANGE: Create a full TodoItem object
            lastId++ // Increment the ID
            val newItem = TodoItem(
                id = lastId,
                taskName = text
            )
            todoList.add(newItem)
            todoText.value = ""
            saveTasks()
        }
    }

    // 5. CHANGE: We pass the whole item to remove
    fun removeTask(item: TodoItem) {
        todoList.remove(item)
        saveTasks()
    }

    // 6. NEW: This function handles toggling the 'isDone' state
    fun toggleDoneStatus(item: TodoItem) {
        // Find the item in the list
        val index = todoList.indexOf(item)
        if (index != -1) {
            // Create a *copy* of the item with the 'isDone' status flipped
            val updatedItem = item.copy(isDone = !item.isDone)
            // Replace the old item with the new one to trigger a UI update
            todoList[index] = updatedItem
            saveTasks()
        }
    }

    // --- PRIVATE HELPER FUNCTIONS ---

    private fun loadTasks() {
        val tasksJson = sharedPreferences.getString(TASKS_KEY, null)
        if (tasksJson != null) {
            // 7. CHANGE: We now load a List<TodoItem>
            val type = object : TypeToken<List<TodoItem>>() {}.type
            val loadedTasks: List<TodoItem> = gson.fromJson(tasksJson, type)
            todoList.clear()
            todoList.addAll(loadedTasks)

            // 8. NEW: Update our ID counter to be higher than any loaded ID
            lastId = loadedTasks.maxOfOrNull { it.id } ?: 0L
        } else {
            // Add some default data if the list is empty
            val defaultTasks = listOf(
                TodoItem(id = 1L, taskName = "Buy milk", isDone = true),
                TodoItem(id = 2L, taskName = "Walk the dog"),
                TodoItem(id = 3L, taskName = "Learn ViewModels")
            )
            todoList.addAll(defaultTasks)
            lastId = 3L // Set the ID counter
        }
    }

    private fun saveTasks() {
        // 9. CHANGE: This works exactly the same, but now saves the list of objects
        val tasksJson = gson.toJson(todoList)
        sharedPreferences.edit()
            .putString(TASKS_KEY, tasksJson)
            .apply()
    }
}