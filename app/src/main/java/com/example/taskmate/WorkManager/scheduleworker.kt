package com.example.taskmate.WorkManager



import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun scheduleSyncWorker(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val request = PeriodicWorkRequestBuilder<MyWorker>(
        15, TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .addTag("sync_tasks_worker")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "SyncTasks",
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}
