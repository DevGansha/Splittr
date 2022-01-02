package com.example.androidproject.models.specificlist

import com.google.gson.annotations.SerializedName

data class ListData(@SerializedName("data")
                    var data: List<Data>?= null)
