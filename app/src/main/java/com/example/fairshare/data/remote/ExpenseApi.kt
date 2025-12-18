package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.CreateExpenseRequest
import com.example.fairshare.data.remote.models.ExpenseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API interface for expense-related endpoints.
 *
 * Provides methods to create new expenses and retrieve all expenses
 * within a specific group.
 */
interface ExpenseApi {

    /**
     * Creates a new expense in the specified group.
     *
     * The expense will be attributed to the currently authenticated user as the payer.
     * The amount will be split equally among all group members.
     *
     * @param groupId Unique identifier of the group to add the expense to.
     * @param body Expense details containing description and amount.
     * @return [Response] containing the created [ExpenseResponse] with server-assigned ID,
     *         or an error response if creation fails.
     */
    @POST("expense/{groupId}")
    suspend fun createExpense(
        @Path("groupId") groupId: String,
        @Body body: CreateExpenseRequest
    ): Response<ExpenseResponse>

    /**
     * Retrieves all expenses for a specific group.
     *
     * Returns expenses in chronological order with details including
     * description, amount, timestamp, and the user who paid.
     *
     * @param groupId Unique identifier of the group to get expenses for.
     * @return [Response] containing a list of [ExpenseResponse] objects,
     *         or an error response if the group doesn't exist or user lacks access.
     */
    @GET("expenses/{groupId}")
    suspend fun getGroupExpenses(
        @Path("groupId") groupId: String
    ): Response<List<ExpenseResponse>>
}
