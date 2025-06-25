package com.example.todolist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ToDoListApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { TaskRepository(database.taskDao()) }
    val viewModel = remember { TaskViewModel(repository) }
    val tasks by viewModel.tasks.collectAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var taskAdded by remember { mutableStateOf(false) }
    val options = listOf("Low", "Moderate", "High")
    var selectedOption by remember { mutableStateOf(options[0]) }
    var showEditorDialog by remember { mutableStateOf(false) }
    var selectedItemForEdit by remember { mutableStateOf<ListItems?>(null) }

    val suggestions = listOf(
        "You could read for 10 minutes.",
        "Maybe plan tomorrow's goals?",
        "Consider organizing your desk.",
        "Take a short walk to refresh.",
        "Review your top 3 priorities."
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "gradientTransition")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientOffset"
    )
    val animatedBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F1B76), Color(0xFF2F3E46), Color(0xFF56A0D3)),
        startY = offset,
        endY = offset + 2000f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBrush)
            .padding(16.dp)
    ) {

        if (tasks.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
            ) {
                TopAppBar(
                    title = {
                        Text(text = "Your tasks (${tasks.size})", color = Color.White, textAlign = TextAlign.Center)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        }

        if (tasks.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Image(
                        painter = painterResource(id = R.drawable.clipboard_with_pen_and_bell_notification_checklist_form_report_checkbox_business_3d_background_illustration),
                        contentDescription = "Empty State",
                        modifier = Modifier.size(300.dp)
                    )
                    Canvas(modifier = Modifier
                        .size(width = 180.dp, height = 40.dp)
                        .offset(y = 20.dp)
                    ) {
                        drawOval(color = Color.Black.copy(alpha = 0.25f))
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 80.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .heightIn(min = 180.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.05f)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✨ No tasks yet!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Looks like you're free right now.\nPerfect time to plan something awesome!",
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(3000L)
                            currentIndex = (currentIndex + 1) % suggestions.size
                        }
                    }

                    AnimatedContent(
                        targetState = suggestions[currentIndex],
                        transitionSpec = {
                            fadeIn(tween(500)) with fadeOut(tween(500))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) { suggestion ->
                        Text(
                            text = "💡 $suggestion",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f, fill = false)
        ) {
            items(tasks) { item ->
                ListItem(
                    item = item,
                    onEdit = {
                        selectedItemForEdit = item
                        showEditorDialog = true
                    },
                    onDelete = {
                        viewModel.deleteTask(item)
                    },
                    onToggleCompleted = {
                        viewModel.updateTask(item.copy(completed = !item.completed))
                    }
                )
            }
        }

        if (showEditorDialog && selectedItemForEdit != null) {
            TaskEditorDialog(
                item = selectedItemForEdit!!,
                onDismiss = { showEditorDialog = false },
                onSave = { editedItem ->
                    viewModel.updateTask(editedItem)
                    showEditorDialog = false
                }
            )
        }

        val shouldAnimateAddButton = tasks.isEmpty() && !taskAdded
        AddTaskButton(
            onClick = { showDialog = true },
            shouldAnimate = shouldAnimateAddButton
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                val nameError = itemName.isBlank()
                                val urgencyError = selectedOption.isBlank()
                                if (!nameError && !urgencyError) {
                                    viewModel.addTask(
                                        ListItems(
                                            id = 0, // Room auto-generates
                                            name = itemName,
                                            urgency = selectedOption
                                        )
                                    )
                                    taskAdded = true
                                    showDialog = false
                                    itemName = ""
                                    selectedOption = options[0]
                                }
                            },
                            enabled = itemName.isNotBlank() && selectedOption.isNotBlank()
                        ) {
                            Text("Add")
                        }

                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                },
                title = { Text("Add Tasks") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            label = { Text("Task Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        UrgencyDropdown(
                            selectedOption = selectedOption,
                            onOptionSelected = { selectedOption = it }
                        )
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrgencyDropdown(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Low", "Moderate", "High")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text("Urgency") },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TaskEditorDialog(
    item: ListItems,
    onSave: (ListItems) -> Unit,
    onDismiss: () -> Unit
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedUrgency by remember { mutableStateOf(item.urgency) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Task name") }
                )
                UrgencyDropdown(selectedOption = editedUrgency,
                    onOptionSelected = { editedUrgency = it}
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(item.copy(name = editedName, urgency = editedUrgency))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun ListItem(
    item: ListItems,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleCompleted: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = if (item.completed) FontWeight.Light else FontWeight.Bold,
                color = if (item.completed) Color.Gray else Color.Black
            )
            Text(
                text = "Urgency: ${item.urgency}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }

        Row {
            Checkbox(
                checked = item.completed,
                onCheckedChange = { onToggleCompleted() }
            )
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.DarkGray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.DarkGray)
            }
        }
    }
}

@Composable
fun AddTaskButton(
    onClick: () -> Unit,
    shouldAnimate: Boolean
) {
    val scale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = if (shouldAnimate) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .navigationBarsPadding(), // ensures visibility above system UI
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(63, 27, 118))
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(28.dp), tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Add a task", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoListPreview() {
    ToDoListApp()
}