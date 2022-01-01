package com.example.androidproject.models.login

import com.google.gson.annotations.SerializedName

data class data (
    @SerializedName("id")
    var id: Int? = null ,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
)