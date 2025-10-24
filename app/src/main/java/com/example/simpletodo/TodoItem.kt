package com.example.simpletodo

// This is our new data model.
// It's a 'data class', which is perfect for holding data.
data class TodoItem(
    val id: Long, // A unique ID to help Compose track each item
    val taskName: String,
    val isDone: Boolean = false // Default to 'not done'
)