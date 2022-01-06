package com.example.androidproject.models.email

import com.google.gson.annotations.SerializedName

data class ResponseData(@SerializedName("data")
                        var data: List<data>?= null)