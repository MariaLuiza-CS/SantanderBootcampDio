package com.example.santanderprojectdio.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.santanderprojectdio.utils.constants.Constants
import com.example.santanderprojectdio.data.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Query(value = "SELECT * FROM ${Constants.TaskConst.TABLE_NAME} ORDER BY id ASC")
    fun readAllData(): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query(value = "DELETE FROM ${Constants.TaskConst.TABLE_NAME}")
    suspend fun deleteAll()
}