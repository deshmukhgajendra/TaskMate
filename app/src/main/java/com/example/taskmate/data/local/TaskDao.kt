package com.example.taskmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmate.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("select * from TaskTable")
     fun getAllTasks(): Flow<List<Task>>

     @Query("select * from TaskTable where priority = 'High Priority'")
     fun getHighPriorityTasks(): Flow<List<Task>>

    @Query("select * from TaskTable where priority = 'Medium Priority'")
     fun getMediumPriorityTasks() : Flow<List<Task>>

    @Query("SELECT * FROM TaskTable WHERE priority = 'Low Priority'")
     fun getLowPriorityTasks(): Flow<List<Task>>

     @Query("select * from tasktable where isSynced = 0")
     fun getUnsyncedTasks(): List<Task>

    @Query("SELECT * FROM TaskTable WHERE dueDate < :today AND isCompleted = 0")
    suspend fun getOverdueTasks(today: String): List<Task>
}