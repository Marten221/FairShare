package com.example.fairshare.domain.model

/**
 * Domain model representing a user's balance summary within a group.
 *
 * Provides an overview of the user's financial position relative to other group members.
 *
 * @property userId Unique identifier of the user.
 * @property userName Display name of the user.
 * @property totalOwes Total amount the user owes to other group members.
 * @property totalIsOwed Total amount other group members owe to the user.
 * @property net Net balance calculated as [totalIsOwed] minus [totalOwes].
 *               Positive value indicates user is owed money overall.
 *               Negative value indicates user owes money overall.
 *               Zero indicates the user is settled up.
 */
data class UserBalance(
    val userId: String,
    val userName: String,
    val totalOwes: Double,
    val totalIsOwed: Double,
    val net: Double,
)

/**
 * Domain model representing a single debt between two users.
 *
 * Captures a directional financial obligation from one user to another.
 *
 * @property fromUserId Unique identifier of the debtor (person who owes money).
 * @property fromUserName Display name of the debtor.
 * @property toUserId Unique identifier of the creditor (person who is owed money).
 * @property toUserName Display name of the creditor.
 * @property amount The debt amount in the group's currency.
 */
data class Debt(
    val fromUserId: String,
    val fromUserName: String,
    val toUserId: String,
    val toUserName: String,
    val amount: Double,
)

/**
 * Domain model containing categorized debt information for the current user.
 *
 * Separates debts into two lists for intuitive display:
 * debts the user owes and debts owed to the user.
 *
 * @property owe List of debts where the current user is the debtor.
 *               These are amounts the user needs to pay to others.
 * @property owed List of debts where the current user is the creditor.
 *                These are amounts others need to pay to the user.
 */
data class UserDebts(
    val owe: List<Debt>,
    val owed: List<Debt>,
)
