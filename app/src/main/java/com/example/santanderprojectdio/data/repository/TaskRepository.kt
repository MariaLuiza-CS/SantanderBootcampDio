package com.example.santanderprojectdio.data.repository

import androidx.lifecycle.LiveData
import com.example.santanderprojectdio.data.model.Task
import com.example.santanderprojectdio.data.room.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    val readAllData:LiveData<List<Task>> = taskDao.readAllData()

    suspend fun addTask(task: Task){
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun deleteAll(){
        taskDao.deleteAll()
    }
}