package com.example.simpletodo // Make sure this matches your package name!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // NEW import
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf // NEW import
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue // NEW import
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simpletodo.ui.theme.SimpleToDoTheme // This should match your theme's package

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleToDoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoScreen()
                }
            }
        }
    }
}

@Composable
fun TodoScreen() {
    val todoList = remember {
        mutableStateListOf("Buy milk", "Walk the dog", "Learn Compose")
    }

    // --- NEW STATE FOR THE TEXT FIELD ---
    // We use 'mutableStateOf' for a single value (like a String)
    // 'text' holds the current value, and 'setText' is the function to change it.
    var text by remember { mutableStateOf("") }
    // --- END OF NEW STATE ---

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            // The 'weight' modifier makes the LazyColumn fill all available space,
            // pushing the Row (which we'll add) to the bottom.
            modifier = Modifier.weight(1f)
        ) {
            items(todoList) { task ->
                TodoItem(taskName = task)
            }
        }

        // --- NEW UI: INPUT FIELD AND BUTTON ---
        // A Row arranges its children horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Add some space above the input row
            horizontalArrangement = Arrangement.SpaceBetween // Spreads children out
        ) {
            // This is the text input field
            OutlinedTextField(
                value = text, // The text to display
                onValueChange = { newText -> text = newText }, // How to update the text state
                label = { Text("New Task") }, // A placeholder label
                modifier = Modifier.weight(1f) // Make the text field take up most of the space
            )

            // This is the button
            Button(
                onClick = {
                    // This is the logic that runs when the button is clicked
                    if (text.isNotBlank()) { // Only add if the text isn't empty
                        todoList.add(text) // Add the new task to our list
                        text = "" // Clear the text field
                    }
                },
                modifier = Modifier.padding(start = 8.dp) // Space between text field and button
            ) {
                Text("Add")
            }
        }
        // --- END OF NEW UI ---
    }
}

@Composable
fun TodoItem(taskName: String) {
    Text(
        text = taskName,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth() // Make each item take the full width
    )
}

@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    SimpleToDoTheme {
        TodoScreen()
    }
}