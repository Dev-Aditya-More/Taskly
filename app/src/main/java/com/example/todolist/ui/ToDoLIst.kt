package com.example.todolist.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R


data class ListItems(val id:Int,
                     var name:String,
                     var urgency: String,
                     var completed: Boolean = false,
                     var isEditing:Boolean = false)

@Composable
fun ToDoListApp() {
    var sitems by remember { mutableStateOf(listOf<ListItems>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    val options = listOf("Low", "Moderate", "High")
    var selectedOption by remember { mutableStateOf(options[0]) }
    var showEditorDialog by remember { mutableStateOf(false) }
    var selectedItemForEdit by remember { mutableStateOf<ListItems?>(null) }

    // Animated gradient
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

        if (sitems.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "${sitems.size}",
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        //  Empty state
        if (sitems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    // The image
                    Image(
                        painter = painterResource(id = R.drawable.clipboard_with_pen_and_bell_notification_checklist_form_report_checkbox_business_3d_background_illustration),
                        contentDescription = "Empty State",
                        modifier = Modifier
                            .size(300.dp)
                    )

                    // The soft spherical shadow below the image
                    Canvas(modifier = Modifier
                        .size(width = 180.dp, height = 40.dp)
                        .offset(y = 20.dp)
                    ) {
                        drawOval(
                            color = Color.Black.copy(alpha = 0.25f)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .padding(top = 100.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EA)) // Soft light gray
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tasks yet — a perfect time to plan something great!",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Tap 'Add Task' to start your productive journey.",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }


            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = false)  // Let it scroll within remaining space
        ) {
            items(sitems) { item ->
                ListItem(
                    item = item,
                    onEdit = {
                        selectedItemForEdit = item
                        showEditorDialog = true
                    },
                    onDelete = {
                        sitems = sitems.filter { it.id != item.id }
                    },
                    onToggleCompleted = {
                        sitems = sitems.map {
                            if (it.id == item.id) it.copy(completed = !it.completed) else it
                        }
                    }
                )
            }

        }

        if (showEditorDialog && selectedItemForEdit != null) {
            TaskEditorDialog(
                item = selectedItemForEdit!!,
                onDismiss = { showEditorDialog = false },
                onSave = { editedItem ->
                    sitems = sitems.map { if (it.id == editedItem.id) editedItem else it }
                    showEditorDialog = false
                }
            )

        }

        // Add Item Button (FAB style)
        AddTaskButton(onClick = { showDialog = true })

        // Add Item Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                val nameError = itemName.isBlank()
                                val urgencyError = selectedOption.isBlank()

                                if (!nameError && !urgencyError) {
                                    val newItem = ListItems(
                                        id = sitems.size + 1,
                                        name = itemName,
                                        urgency = selectedOption // Change this field in your data class
                                    )
                                    sitems = sitems + newItem
                                    showDialog = false
                                    itemName = ""
                                    selectedOption = ""
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
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
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun AddTaskButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale), // ✅ Apply the animated scale here
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add",
            modifier = Modifier.size(28.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Add a task",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }

}



@Preview(showBackground = true)
@Composable
fun ToDoListPreview() {
    ToDoListApp()
}