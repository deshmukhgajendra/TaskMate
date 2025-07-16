package com.example.taskmate.WorkManager

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskmate.R
import com.example.taskmate.data.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository,
    private val notificationManager: NotificationManager,
    private val notificationBuilder: NotificationCompat.Builder
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val overdueTasks = repository.getOverdueTasks()

        if (overdueTasks.isNotEmpty()) {
            overdueTasks.forEach { task ->
                notificationManager.notify(
                    task.id.hashCode(), // Unique ID
                    notificationBuilder
                        .setContentTitle("Overdue: ${task.title}")
                        .setContentText("Due on: ${task.dueDate}")
                        .build()
                )
            }
        }

        return Result.success()
    }
}