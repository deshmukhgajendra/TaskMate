package com.example.taskmate.utils

import com.example.taskmate.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Task.isOverdue(): Boolean {
    if (this.isCompleted) return false // Not overdue if completed

    return try {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val due = format.parse(this.dueDate)
        due != null && due.before(Date())
    } catch (e: Exception) {
        false
    }
}