package com.example.androidproject.models.signup

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("code")
    var code: Int?= null,
    @SerializedName("status")
    var status: Int?= null,
    @SerializedName("data")
    var data: String?= null
)
