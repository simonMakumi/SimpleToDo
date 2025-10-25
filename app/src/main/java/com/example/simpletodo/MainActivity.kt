package com.example.simpletodo // Make sure this matches your package name!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable // NEW import
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height // NEW import
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions // NEW import
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // NEW import
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // NEW import
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester // NEW import
import androidx.compose.ui.focus.focusRequester // NEW import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager // NEW import
import androidx.compose.ui.text.input.ImeAction // NEW import
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
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

    // 1. Get the new editing state from the ViewModel
    val editingId by viewModel.currentlyEditingId
    val editingText by viewModel.currentlyEditingText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items = viewModel.todoList, key = { it.id }) { task ->
                TodoItem(
                    item = task,
                    // 2. Check if this specific item is the one being edited
                    isEditing = (task.id == editingId),
                    // 3. Pass the current edit text
                    editText = editingText,
                    onDelete = { viewModel.removeTask(task) },
                    onCheckedChange = { viewModel.toggleDoneStatus(task) },
                    // 4. Pass the new event handlers
                    onStartEdit = { viewModel.onStartEdit(task) },
                    onEditTextChanged = { viewModel.onEditTextChanged(it) },
                    onSaveEdit = { viewModel.onSaveEdit() }
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
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next // Changes keyboard button from "Done" to "Next"
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

// --- HEAVILY UPDATED TODO ITEM COMPOSABLE ---
@Composable
fun TodoItem(
    item: TodoItem,
    isEditing: Boolean,
    editText: String,
    onDelete: () -> Unit,
    onCheckedChange: () -> Unit,
    onStartEdit: () -> Unit,
    onEditTextChanged: (String) -> Unit,
    onSaveEdit: () -> Unit
) {
    // This is used to request focus (e.g., show the keyboard)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isDone,
            onCheckedChange = { onCheckedChange() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        // --- NEW CONDITIONAL UI ---
        if (isEditing) {
            // This 'LaunchedEffect' runs when 'isEditing' becomes true
            // It requests focus for the TextField, popping up the keyboard
            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }

            // This is the TextField for editing
            OutlinedTextField(
                value = editText,
                onValueChange = { onEditTextChanged(it) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done // The keyboard button will say "Done"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSaveEdit() // Call save when "Done" is pressed
                        focusManager.clearFocus() // Hide the keyboard
                    }
                ),
                // --- THIS IS THE CORRECTED MODIFIER (Fixed from last time) ---
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .height(50.dp)
            )

        } else {
            // This is the normal Text display
            Text(
                text = item.taskName,
                textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None,
                color = if (item.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onStartEdit() } // Make the text clickable to start editing
            )
        }
        // --- END OF CONDITIONAL UI ---

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
        Text("Preview may not work with ViewModel")
    }
}