package com.example.fairshare.data.remote.models

/**
 * API model representing a user in the system.
 *
 * Used to identify group owners and members in API responses.
 *
 * @property id Unique server-assigned identifier for the user.
 * @property email User's email address, also serves as their display identifier.
 */
data class User(
    val id: String,
    val email: String,
)

/**
 * Request body for creating a new expense sharing group.
 *
 * @property name Display name for the group (e.g., "Vacation Trip", "Apartment Expenses").
 */
data class GroupRequest(
    val name: String,
)

/**
 * API response model for a group with full details.
 *
 * @property id Unique server-assigned identifier for the group.
 * @property name Display name of the group.
 * @property owner The user who created the group and has administrative privileges.
 * @property members List of users who have joined the group (excluding the owner).
 */
data class GroupResponse(
    val id: String,
    val name: String,
    val owner: User,
    val members: List<User>,
)
