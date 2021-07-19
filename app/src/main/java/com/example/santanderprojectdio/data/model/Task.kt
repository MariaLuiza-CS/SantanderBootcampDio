package com.example.santanderprojectdio.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.santanderprojectdio.utils.constants.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Constants.TaskConst.TABLE_NAME)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int ,
    val title: String,
    val hour: String,
    val date: String,
    val description: String
):Parcelable