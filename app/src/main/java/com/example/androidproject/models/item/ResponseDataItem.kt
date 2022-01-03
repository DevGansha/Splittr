package com.example.androidproject.models.item

import com.example.androidproject.models.item.data
import com.google.gson.annotations.SerializedName

data class ResponseDataItem(
    @SerializedName("data")
    var data: List<data>?= null
)
