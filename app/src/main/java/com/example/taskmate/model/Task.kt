package com.example.taskmate.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TaskTable")
data class Task(
    @PrimaryKey(autoGenerate = true) val id :Int =0,
    val title :String,
    val description:String,
    val dueDate :String,
    val priority:String,
    val isCompleted:Boolean,
    val isSynced:Boolean = false
)
