package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class EmployeeNewRequestData(
    @SerializedName("ssn")
    var ssn: String,

    @SerializedName("firstName")
    var firstName: String,

    @SerializedName("lastName")
    var lastName: String,

    @SerializedName("address")
    var address: String,

    @SerializedName("state")
    var state: String,

    @SerializedName("zipcode")
    var zipcode: String,

    @SerializedName("country")
    var country: String,

    @SerializedName("age")
    var age: String,

    @SerializedName("salary")
    var salary: String,

    //var suport:Support
)