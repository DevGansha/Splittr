package com.example.androidproject.models.signup

import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("code")
    var code: Int?= null,
    @SerializedName("status")
    var status: Int?= null,
    @SerializedName("data")
    var data: String?= null
)
