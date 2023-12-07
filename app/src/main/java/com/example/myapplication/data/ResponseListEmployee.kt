package com.example.myapplication.data

import com.google.gson.annotations.SerializedName


data class ResponseListEmployee(
    @SerializedName("data")
    var data: List<EmployeeData>,

    @SerializedName("status")
    var status: Int,

    @SerializedName("message")
    var message: String,
)