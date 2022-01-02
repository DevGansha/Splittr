package com.example.androidproject.models.list

import com.google.gson.annotations.SerializedName

data class data(
    @SerializedName("id")
    var id: Int,
    @SerializedName("list_name")
    var list_name: String,
    @SerializedName("omschrijving")
    var omschrijving: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("creation_date")
    var creation_date: String
)
