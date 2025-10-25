package com.example.simpletodo

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TodoViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private const val PREFS_NAME = "TodoPrefs"
        private const val TASKS_KEY = "TaskList"
    }

    // --- STATE ---
    val todoList = mutableStateListOf<TodoItem>()
    val todoText = mutableStateOf("")

    // --- EDITING STATE ---
    val currentlyEditingId = mutableStateOf<Long?>(null)
    val currentlyEditingText = mutableStateOf("")

    private val sharedPreferences = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

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
            lastId++
            val newItem = TodoItem(
                id = lastId,
                taskName = text
            )
            todoList.add(newItem)
            todoText.value = ""
            saveTasks()
        }
    }

    fun removeTask(item: TodoItem) {
        todoList.remove(item)
        saveTasks()
    }

    fun toggleDoneStatus(item: TodoItem) {
        val index = todoList.indexOf(item)
        if (index != -1) {
            val updatedItem = item.copy(isDone = !item.isDone)
            todoList[index] = updatedItem
            saveTasks()
        }
    }

    // --- EDITING FUNCTIONS ---

    fun onStartEdit(item: TodoItem) {
        currentlyEditingId.value = item.id
        currentlyEditingText.value = item.taskName
    }

    fun onEditTextChanged(newText: String) {
        currentlyEditingText.value = newText
    }

    fun onSaveEdit() {
        val idToSave = currentlyEditingId.value ?: return
        val newText = currentlyEditingText.value

        val index = todoList.indexOfFirst { it.id == idToSave }
        if (index != -1 && newText.isNotBlank()) {
            val updatedItem = todoList[index].copy(taskName = newText)
            todoList[index] = updatedItem
            saveTasks()
        }

        currentlyEditingId.value = null
        currentlyEditingText.value = ""
    }

    // --- PRIVATE HELPER FUNCTIONS ---

    private fun loadTasks() {
        val tasksJson = sharedPreferences.getString(TASKS_KEY, null)
        if (tasksJson != null) {
            val type = object : TypeToken<List<TodoItem>>() {}.type
            val loadedTasks: List<TodoItem> = gson.fromJson(tasksJson, type)
            todoList.clear()
            todoList.addAll(loadedTasks)
            lastId = loadedTasks.maxOfOrNull { it.id } ?: 0L
        } else {
            val defaultTasks = listOf(
                TodoItem(id = 1L, taskName = "Buy milk", isDone = true),
                TodoItem(id = 2L, taskName = "Walk the dog"),
                TodoItem(id = 3L, taskName = "Learn ViewModels")
            )
            todoList.addAll(defaultTasks)
            lastId = 3L
        }
    }

    private fun saveTasks() {
        val tasksJson = gson.toJson(todoList)
        // Use the KTX 'edit' function
        sharedPreferences.edit {
            putString(TASKS_KEY, tasksJson)
        }
    }
}