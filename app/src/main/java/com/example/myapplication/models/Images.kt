package com.example.myapplication.models

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Images(
    @Embedded
    @SerializedName("original")
    val url : Original
)