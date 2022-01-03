package com.example.androidproject.models.item

import com.google.gson.annotations.SerializedName

data class data(
    @SerializedName("id")
    var id: Int?= null,
    @SerializedName("user_id")
    var user_id: Int?= null,
    @SerializedName("list_id")
    var list_id: Int?= null,
    @SerializedName("datum")
    var datum: String?= null,
    @SerializedName("waarde")
    var waarde: String?= null,
    @SerializedName("omschrijving")
    var omschrijving: String?= null,
    @SerializedName("username")
    var username: String?= null
)
