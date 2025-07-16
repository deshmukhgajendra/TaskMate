package com.example.taskmate.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.room.Database
import androidx.room.Room
import androidx.work.WorkManager
import com.example.taskmate.R
import com.example.taskmate.data.Repository
import com.example.taskmate.data.local.TaskDao
import com.example.taskmate.data.local.TaskDatabase
import com.example.taskmate.data.remote.TaskApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context):TaskDatabase{
        return Room.databaseBuilder(context ,
            TaskDatabase::class.java ,
            "TaskDatabase")
            .fallbackToDestructiveMigration().
            build()
    }

    @Provides
    @Singleton
    fun providesRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://6876623f814c0dfa653be169.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesTaskInterface(retrofit: Retrofit):TaskApiService{
        return retrofit.create(TaskApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesRepository(dao: TaskDao,taskApiService: TaskApiService):Repository{
        return Repository(dao,taskApiService)
    }

    @Provides
    @Singleton
    fun providesDao(taskDatabase: TaskDatabase): TaskDao{
        return taskDatabase.taskDao()
    }

    @Singleton
    @Provides
    fun providesWorkManager(app: Application):WorkManager{
        return WorkManager.getInstance(app)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "taskId",
                "NotificationChannel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for overdue tasks"
            }
            notificationManager.createNotificationChannel(channel)
        }

        return notificationManager
    }

    @Provides
    @Singleton
    fun provideNotificationBuilder(
       @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "taskId")
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // change to your icon
            .setContentTitle("Overdue Task!")
            .setContentText("You have an overdue task.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }
}