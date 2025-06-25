package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class ListItems(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val urgency: String,
    val completed: Boolean = false
)