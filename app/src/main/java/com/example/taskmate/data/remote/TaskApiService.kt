package com.example.taskmate.data.remote

import com.example.taskmate.model.Task
import com.example.taskmate.model.TaskModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApiService {

    @GET("tasks")
    suspend fun getTaks(): List<Task>

    @POST("tasks")
    suspend fun uploadTask(@Body task: TaskModel): Response<Unit>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>

}