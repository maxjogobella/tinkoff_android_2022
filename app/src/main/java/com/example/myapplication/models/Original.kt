package com.example.myapplication.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Original(
    @Expose
    @SerializedName("url")
    val url: String
)