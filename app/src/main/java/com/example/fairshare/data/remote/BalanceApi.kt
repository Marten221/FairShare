package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.UserBalanceResponse
import com.example.fairshare.data.remote.models.UserDebtsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *  API interface for balance and debt-related endpoints.
 *
 * Provides methods to retrieve the current user's balance summary
 * and detailed debt information within a specific group.
 */
interface BalanceApi {

    /**
     * Retrieves the current user's balance summary for a specific group.
     *
     * The balance includes total amount owed, total amount owed to the user,
     * and the net balance (positive means user is owed money, negative means user owes).
     *
     * @param groupId Unique identifier of the group to get balance for.
     * @return [Response] containing [UserBalanceResponse] with balance details,
     *         or an error response if the group doesn't exist or user lacks access.
     */
    @GET("userbalance/{groupId}")
    suspend fun getUserBalance(
        @Path("groupId") groupId: String
    ): Response<UserBalanceResponse>

    /**
     * Retrieves detailed debt information for the current user in a specific group.
     *
     * Returns two lists: debts the user owes to others, and debts others owe to the user.
     *
     * @param groupId Unique identifier of the group to get debts for.
     * @return [Response] containing [UserDebtsResponse] with detailed debt breakdowns,
     *         or an error response if the group doesn't exist or user lacks access.
     */
    @GET("userdebts/{groupId}")
    suspend fun getUserDebts(
        @Path("groupId") groupId: String
    ): Response<UserDebtsResponse>
}
