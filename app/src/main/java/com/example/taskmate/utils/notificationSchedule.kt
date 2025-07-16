package com.example.taskmate.utils

import com.example.taskmate.WorkManager.NotificationWorker


import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun scheduleOverdueTaskNotifier(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
        6, TimeUnit.HOURS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "OverdueTaskNotifier",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
