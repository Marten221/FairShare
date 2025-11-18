package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.CreateExpenseRequest
import com.example.fairshare.data.remote.models.ExpenseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseApi {

    @POST("expense/{groupId}")
    suspend fun createExpense(
        @Path("groupId") groupId: String,
        @Body body: CreateExpenseRequest
    ): Response<ExpenseResponse>

    @GET("expenses/{groupId}")
    suspend fun getGroupExpenses(
        @Path("groupId") groupId: String
    ): Response<List<ExpenseResponse>>
}
