package com.example.taskmate.userInterface

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmate.data.Repository
import com.example.taskmate.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){


    var selectedTask by mutableStateOf<Task?>(null)
        private set

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList:StateFlow<List<Task>> = _taskList

    private val _highTasks = MutableStateFlow<List<Task>>(emptyList())
    val highTasks: StateFlow<List<Task>> = _highTasks

    private val _mediumTasks = MutableStateFlow<List<Task>>(emptyList())
    val mediumTasks: StateFlow<List<Task>> = _mediumTasks

    private val _lowTasks = MutableStateFlow<List<Task>>(emptyList())
    val lowTasks: StateFlow<List<Task>> = _lowTasks




    fun fetchAllPriorityTasks() {
        viewModelScope.launch {
            launch {
                repository.getHighPriorityTasks().collect { high ->
                    _highTasks.value = high
                    //Log.d("high", "HighTasks: ${high.size}")
                }
            }
            launch {
                repository.getMediumPriorityTasks().collect { medium ->
                    _mediumTasks.value = medium
                    //Log.d("medium", "Medium prioarity : ${medium.size}")
                }
            }
            launch {
                repository.getLowPriorityTasks().collect { low ->
                    _lowTasks.value = low
                    //Log.d("low", "Low Prioritytasks: ${low.size}")
                }
            }
        }
    }
    fun onEditClick(task: Task){
        selectedTask=task
    }

    fun getAllTasks(){
        viewModelScope.launch {
             repository.getAllTasks().collect{task ->
                 _taskList.value=task
            }

        }
    }
    fun insertTask(task: Task){
        viewModelScope.launch {

            repository.insertTask(task)
           // repository.insertTaskToApi(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            repository.updateTask(task)
            selectedTask=null
            getAllTasks()
        }
    }
}