package com.example.myapplication.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gif_table")
data class Data(
    @PrimaryKey
    @SerializedName("id")
    val id : String,
    @SerializedName("title")
    val title : String,
    @Embedded
    @SerializedName("images")
    val images : Images
)