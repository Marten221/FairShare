package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.UserBalance
import com.example.fairshare.domain.model.UserDebts

/**
 * Repository interface for balance and debt operations.
 *
 * Defines the contract for retrieving user balance summaries and detailed
 * debt information within expense sharing groups.
 */
interface BalanceRepository {

    /**
     * Retrieves the current user's balance summary for a specific group.
     *
     * @param groupId Unique identifier of the group.
     * @return [Result.success] containing [UserBalance] with balance details,
     *         or [Result.failure] with an exception if retrieval fails.
     */
    suspend fun getUserBalance(groupId: String): Result<UserBalance>

    /**
     * Retrieves detailed debt information for the current user in a group.
     *
     * Returns categorized lists of debts the user owes and debts owed to the user.
     *
     * @param groupId Unique identifier of the group.
     * @return [Result.success] containing [UserDebts] with detailed debt breakdowns,
     *         or [Result.failure] with an exception if retrieval fails.
     */
    suspend fun getUserDebts(groupId: String): Result<UserDebts>
}
