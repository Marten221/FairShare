package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.AuthRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @POST("public/register")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body body: AuthRequest): Response<String>

    @POST("public/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body body: AuthRequest): Response<String>
}