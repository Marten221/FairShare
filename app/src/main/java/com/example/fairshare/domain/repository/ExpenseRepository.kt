package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.Expense

/**
 * Repository interface for expense operations.
 *
 * Defines the contract for creating and retrieving expenses within groups.
 * Implementations handle the actual network communication and data mapping.
 */
interface ExpenseRepository {
    /**
     * Creates a new expense in the specified group.
     *
     * The expense will be attributed to the currently authenticated user
     * and split among all group members.
     *
     * @param groupId Unique identifier of the group to add the expense to.
     * @param description User provided description of the expense.
     * @param amount Total expense amount to be split.
     * @return [Result.success] containing the created [Expense] with server assigned ID,
     *         or [Result.failure] with an exception if creation fails.
     */
    suspend fun createExpense(
        groupId: String,
        description: String,
        amount: Double,
    ): Result<Expense>

    /**
     * Retrieves all expenses for a specific group.
     *
     * @param groupId Unique identifier of the group.
     * @return [Result.success] containing a list of [Expense] objects,
     *         or [Result.failure] with an exception if retrieval fails.
     */
    suspend fun getGroupExpenses(groupId: String): Result<List<Expense>>
}
