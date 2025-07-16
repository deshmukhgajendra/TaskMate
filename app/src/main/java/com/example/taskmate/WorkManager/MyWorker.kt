package com.example.taskmate.WorkManager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskmate.data.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    val repository: Repository
):CoroutineWorker(appContext,workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            repository.uploadUnsyncedTasks()
            Result.success()
        }catch (e:Exception){
            Result.failure()
        }
    }
}