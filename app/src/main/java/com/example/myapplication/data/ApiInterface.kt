package com.example.myapplication.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    @GET("employee")
    suspend fun getAllEmployee(): Response<ResponseListEmployee>

    @POST("employee")
    suspend fun saveEmployee(@Body employeeData: EmployeeNewRequestData): Response<ResponseEmployee>

    @PUT("employee/{id}")
    suspend fun updateEmployee(@Path("id") id: String, @Body employeeData: EmployeeData): Response<ResponseEmployee>

    @DELETE("employee/{id}")
    suspend fun deleteEmployee(@Path("id") id: String): Response<ResponseEmployee>
}