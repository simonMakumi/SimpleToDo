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
import androidx.compose.material3.Checkbox // NEW import
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme // NEW import
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // NEW import
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration // NEW import
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    TodoScreen(viewModel = viewModel())
                }
            }
        }
    }
}

@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val text by viewModel.todoText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // 1. CHANGE: The key is now the item's ID
            items(items = viewModel.todoList, key = { it.id }) { task ->
                TodoItem(
                    // 2. CHANGE: Pass the whole item
                    item = task,
                    onDelete = {
                        // 3. CHANGE: Pass the item to remove
                        viewModel.removeTask(task)
                    },
                    // 4. NEW: Add the check change handler
                    onCheckedChange = {
                        viewModel.toggleDoneStatus(task)
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
                onValueChange = { viewModel.onTodoTextChange(it) },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text
                )
            )

            Button(
                onClick = { viewModel.addTask() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }
    }
}

// --- UPDATED TODO ITEM COMPOSABLE ---
@Composable
fun TodoItem(
    item: TodoItem, // 5. CHANGE: Receive the full item
    onDelete: () -> Unit,
    onCheckedChange: () -> Unit // 6. NEW: Receive the check handler
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // A little less padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 7. NEW: The Checkbox
        Checkbox(
            checked = item.isDone,
            onCheckedChange = { onCheckedChange() } // Call the handler
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 8. NEW: Strikethrough and color change
        Text(
            text = item.taskName,
            textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None,
            color = if (item.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface,
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
// --- END OF UPDATED COMPOSABLE ---


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    SimpleToDoTheme {
        // Preview won't work easily with the ViewModel
        Text("Preview may not work with ViewModel")
    }
}