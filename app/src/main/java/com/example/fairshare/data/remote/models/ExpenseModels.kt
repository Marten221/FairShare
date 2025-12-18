package com.example.fairshare.data.remote.models

/**
 * API response model for an expense record.
 *
 * @property id Unique server-assigned identifier for the expense.
 * @property description User-provided description of what the expense was for.
 * @property amount The expense amount in the group's currency.
 * @property timestamp ISO 8601 formatted timestamp of when the expense was created
 *                     (e.g., "2025-11-19T13:45:12").
 * @property owner The user who paid for this expense and is requesting reimbursement.
 */
data class ExpenseResponse(
    val id: String,
    val description: String,
    val amount: Double,
    val timestamp: String,
    val owner: User
)

/**
 * Request body for creating a new expense.
 *
 * @property description User-provided description of what the expense is for.
 * @property amount The total expense amount to be split among group members.
 */
data class CreateExpenseRequest(
    val description: String,
    val amount: Double
)