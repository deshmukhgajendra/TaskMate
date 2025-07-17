package com.example.taskmate.data

import android.util.Log
import com.example.taskmate.data.local.TaskDao
import com.example.taskmate.data.remote.TaskApiService
import com.example.taskmate.model.Task
import com.example.taskmate.model.TaskModel
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao : TaskDao,
    private val taskApiService: TaskApiService
) {

    // database
    suspend fun insertTask(task: Task) = dao.insertTask(task)

    suspend fun deleteTask(task: Task){

        try {
            val response = taskApiService.deleteTask(task.id.toString())
            if (response.isSuccessful) {
                dao.deleteTask(task)
            }
        }catch (e :Exception){
            dao.deleteTask(task)
        }
    }

    suspend fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    suspend fun getHighPriorityTasks():Flow<List<Task>> = dao.getHighPriorityTasks()

    suspend fun getMediumPriorityTasks():Flow<List<Task>> = dao.getMediumPriorityTasks()

    suspend fun getLowPriorityTasks():Flow<List<Task>> = dao.getLowPriorityTasks()

    suspend fun updateTask(task: Task) = dao.updateTask(task)

    suspend fun uploadUnsyncedTasks() {
        val unsyncedTasks = dao.getUnsyncedTasks()

        unsyncedTasks.forEach { task ->
            try {
                val taskModel = TaskModel(
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    priority = task.priority,
                    isCompleted = task.isCompleted
                )

                val response = taskApiService.uploadTask(taskModel)

                if (response.isSuccessful) {
                    dao.updateTask(task.copy(isSynced = true))
                } else {
                    Log.e("gajendra", "Upload failed : ${task.title}, response: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("gajendra", "Exception on uploading task: ${task.title}", e)
            }
        }
    }

    suspend fun getOverdueTasks(): List<Task> {
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        return dao.getOverdueTasks(today)
    }


}