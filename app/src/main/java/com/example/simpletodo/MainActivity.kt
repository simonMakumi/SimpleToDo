package com.example.simpletodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card // NEW import
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
    val editingId by viewModel.currentlyEditingId
    val editingText by viewModel.currentlyEditingText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            // Add some spacing between the cards
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = viewModel.todoList, key = { it.id }) { task ->
                TodoItem(
                    item = task,
                    isEditing = (task.id == editingId),
                    editText = editingText,
                    onDelete = { viewModel.removeTask(task) },
                    onCheckedChange = { viewModel.toggleDoneStatus(task) },
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
                    imeAction = ImeAction.Next
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
    item: TodoItem,
    isEditing: Boolean,
    editText: String,
    onDelete: () -> Unit,
    onCheckedChange: () -> Unit,
    onStartEdit: () -> Unit,
    onEditTextChanged: (String) -> Unit,
    onSaveEdit: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // 1. Wrap the entire item in a Card
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // 2. Add padding *inside* the card
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isDone,
                onCheckedChange = { onCheckedChange() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (isEditing) {
                LaunchedEffect(key1 = Unit) {
                    focusRequester.requestFocus()
                }

                OutlinedTextField(
                    value = editText,
                    onValueChange = { onEditTextChanged(it) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSaveEdit()
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .height(50.dp)
                )

            } else {
                Text(
                    text = item.taskName,
                    textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (item.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStartEdit() }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task"
                )
            }
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