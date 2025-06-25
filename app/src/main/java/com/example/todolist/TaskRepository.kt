package com.example.todolist

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    val tasks: Flow<List<ListItems>> = dao.getAllTasks()


    suspend fun insert(task: ListItems) {
        dao.insertTask(task)
    }
    suspend fun delete(task: ListItems) {
        dao.deleteTask(task)
    }
    suspend fun update(task: ListItems) {
        dao.updateTask(task)
    }
}