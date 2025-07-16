package com.example.taskmate.model

data class TaskModel(
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: String,
    val isCompleted: Boolean
)
