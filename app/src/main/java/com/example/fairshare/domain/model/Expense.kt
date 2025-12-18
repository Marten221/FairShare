package com.example.fairshare.domain.model

/**
 * Domain model representing an expense within a group.
 *
 * An expense is a payment made by one group member that should be
 * split among all members. The payer is identified by [ownerEmail].
 *
 * @property id Unique server-assigned identifier for the expense.
 * @property description User-provided description of what the expense was for
 *                       (e.g., "Dinner at restaurant", "Groceries").
 * @property amount The total expense amount in the group's currency.
 * @property ownerEmail Email address of the user who paid for this expense.
 * @property date Human-readable date string when the expense was created
 *                (formatted as "dd.MM.yyyy").
 */
data class Expense(
    val id: String,
    val description: String,
    val amount: Double,
    val ownerEmail: String,
    val date: String
)