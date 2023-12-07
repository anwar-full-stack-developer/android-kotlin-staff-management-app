package com.example.myapplication.data

import com.google.gson.annotations.SerializedName


data class ResponseEmployee(
    @SerializedName("data")
    var data: EmployeeData?,

    @SerializedName("status")
    var status: Int,

    @SerializedName("msg")
    var message: String?,
)