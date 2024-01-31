package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Datum(
    @SerializedName("data")
    val data : Data
)