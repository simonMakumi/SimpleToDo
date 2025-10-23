package com.example.simpletodo // Make sure this matches your package name!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer // NEW import
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width // NEW import
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions // NEW import
import androidx.compose.material.icons.Icons // NEW import
import androidx.compose.material.icons.filled.Delete // NEW import
import androidx.compose.material3.Button
import androidx.compose.material3.Icon // NEW import
import androidx.compose.material3.IconButton // NEW import
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment // NEW import
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization // NEW import
import androidx.compose.ui.text.input.KeyboardType // NEW import
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
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // 'items' is a bit different now, we pass the list and a key
            // The key helps Compose efficiently update the list
            items(items = todoList, key = { it }) { task ->
                TodoItem(
                    taskName = task,
                    onDelete = {
                        // This is the logic that runs when a delete button is clicked
                        todoList.remove(task)
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { newText -> text = newText },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f),
                // --- NEW KEYBOARD OPTIONS ---
                keyboardOptions = KeyboardOptions(
                    // Automatically capitalize the first letter of a sentence
                    capitalization = KeyboardCapitalization.Sentences,
                    // Tell the keyboard this is plain text
                    keyboardType = KeyboardType.Text
                )
                // --- END OF NEW OPTIONS ---
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        todoList.add(text)
                        text = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }
    }
}

// --- UPDATED TODO ITEM COMPOSABLE ---
@Composable
fun TodoItem(taskName: String, onDelete: () -> Unit) { // Now takes an 'onDelete' function
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically // Aligns text and icon nicely
    ) {
        // Task text
        Text(
            text = taskName,
            modifier = Modifier.weight(1f) // Text takes up most of the space
        )

        Spacer(modifier = Modifier.width(8.dp)) // A little space

        // Delete button
        IconButton(onClick = onDelete) { // Calls the onDelete function when clicked
            Icon(
                imageVector = Icons.Default.Delete, // The standard delete icon
                contentDescription = "Delete Task" // For accessibility
            )
        }
    }
}
// --- END OF UPDATED COMPOSABLE ---


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    SimpleToDoTheme {
        TodoScreen()
    }
}