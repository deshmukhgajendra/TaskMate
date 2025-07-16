package com.example.taskmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmate.model.Task


@Database(entities = [Task::class], version = 4, exportSchema = false)
abstract class TaskDatabase:RoomDatabase() {

    abstract fun taskDao(): TaskDao
}