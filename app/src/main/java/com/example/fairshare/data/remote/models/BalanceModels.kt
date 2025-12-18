package com.example.fairshare.data.remote.models

/**
 * API response model for a user's balance summary within a group.
 *
 * @property userId Unique identifier of the user.
 * @property userName Display name of the user.
 * @property totalOwes Total amount the user owes to other group members.
 * @property totalIsOwed Total amount other group members owe to the user.
 * @property net Net balance calculated as [totalIsOwed] minus [totalOwes].
 *               Positive value means user is owed money, negative means user owes.
 */
data class UserBalanceResponse(
    val userId: String,
    val userName: String,
    val totalOwes: Double,
    val totalIsOwed: Double,
    val net: Double
)

/**
 * API response model for a single debt between two users.
 *
 * Represents a directional debt from one user to another within a group.
 *
 * @property fromUserId Unique identifier of the debtor (person who owes).
 * @property fromUserName Display name of the debtor.
 * @property toUserId Unique identifier of the creditor (person who is owed).
 * @property toUserName Display name of the creditor.
 * @property amount The debt amount in the group's currency.
 */
data class DebtResponse(
    val fromUserId: String,
    val fromUserName: String,
    val toUserId: String,
    val toUserName: String,
    val amount: Double
)

/**
 * API response model containing all debts for the current user in a group.
 *
 * Separates debts into two categories for easy UI rendering.
 *
 * @property owe List of debts where the current user is the debtor (owes others).
 * @property owed List of debts where the current user is the creditor (others owe them).
 */
data class UserDebtsResponse(
    val owe: List<DebtResponse>,
    val owed: List<DebtResponse>
)
