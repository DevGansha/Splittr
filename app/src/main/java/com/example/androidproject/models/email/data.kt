package com.example.androidproject.models.email

import com.google.gson.annotations.SerializedName

data class data(
    @SerializedName("user_id")
    var user_id: Int,
    @SerializedName("list_id")
    var list_id: Int
)
