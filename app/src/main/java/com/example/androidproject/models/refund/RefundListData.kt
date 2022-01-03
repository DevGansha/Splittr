package com.example.androidproject.models.refund

import com.google.gson.annotations.SerializedName

data class RefundListData(@SerializedName("data")
                    var data: List<data>?= null)
