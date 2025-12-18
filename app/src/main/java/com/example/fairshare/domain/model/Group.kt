package com.example.fairshare.domain.model

/**
 * Domain model representing an expense sharing group.
 *
 * A group is a collection of users who share expenses together.
 * Members can add expenses that are automatically split among all participants.
 *
 * @property id Unique server-assigned identifier for the group.
 *              Can be shared with others to allow them to join.
 * @property name Display name of the group chosen by the creator
 *                (e.g., "Vacation Trip", "Apartment Expenses").
 * @property memberCount Total number of users in the group, including the owner.
 */
data class Group(
    val id: String,
    val name: String,
    val memberCount: Int,
)
