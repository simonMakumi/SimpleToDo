package com.example.simpletodo // Make sure this matches your package name!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // NEW import
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // NEW import
import com.example.simpletodo.ui.theme.SimpleToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleToDoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // We pass the ViewModel down to our screen
                    TodoScreen(viewModel = viewModel())
                }
            }
        }
    }
}

// --- TodoScreen is now much simpler! ---
@Composable
fun TodoScreen(viewModel: TodoViewModel) { // It receives the ViewModel

    // Get the current text from the ViewModel.
    // 'by' is a delegate that unwraps the .value for us.
    val text by viewModel.todoText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // We get the list directly from the ViewModel
            items(items = viewModel.todoList, key = { it }) { task ->
                TodoItem(
                    taskName = task,
                    onDelete = {
                        // We call the ViewModel's function
                        viewModel.removeTask(task)
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
                // Read the value from our 'text' variable
                value = text,
                // Call the ViewModel function when the text changes
                onValueChange = { viewModel.onTodoTextChange(it) },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text
                )
            )

            Button(
                // Call the ViewModel function on click
                onClick = { viewModel.addTask() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun TodoItem(taskName: String, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = taskName,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    SimpleToDoTheme {
        // We can't use a real ViewModel in a Preview,
        // so we'll have to adjust this later if we want the preview to work.
        // For now, it will be blank, but the app itself will run.
        // We could create a "fake" ViewModel for the preview if we wanted.
        Text("Preview may not work with ViewModel")
    }
}