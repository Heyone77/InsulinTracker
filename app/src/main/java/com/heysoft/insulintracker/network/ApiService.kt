package com.heysoft.insulintracker.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class UserRequest(val uuid: String)
data class UserResponse(val message: String)

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/add")
    fun addUser(@Body request: UserRequest): Call<UserResponse>
}