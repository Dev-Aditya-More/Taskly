package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<ListItems>>

    @Upsert
    suspend fun insertTask(task: ListItems)

    @Delete
    suspend fun deleteTask(task: ListItems)

    @Update
    suspend fun updateTask(task: ListItems)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Int)
}