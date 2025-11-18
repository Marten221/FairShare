package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.UserBalanceResponse
import com.example.fairshare.data.remote.models.UserDebtsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BalanceApi {

    @GET("userbalance/{groupId}")
    suspend fun getUserBalance(
        @Path("groupId") groupId: String
    ): Response<UserBalanceResponse>

    @GET("userdebts/{groupId}")
    suspend fun getUserDebts(
        @Path("groupId") groupId: String
    ): Response<UserDebtsResponse>
}
