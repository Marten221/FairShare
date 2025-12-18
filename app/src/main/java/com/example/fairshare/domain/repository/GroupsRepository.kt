package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.Group

/**
 * Repository interface for group management operations.
 *
 * Defines the contract for creating, joining, and retrieving expense sharing groups.
 * Implementations handle the actual network communication and data mapping.
 */
interface GroupsRepository {
    /**
     * Creates a new expense sharing group.
     *
     * The authenticated user becomes the owner of the newly created group.
     *
     * @param name Display name for the new group.
     * @return [Result.success] containing the created [Group] with server-assigned ID,
     *         or [Result.failure] with an exception if creation fails.
     */
    suspend fun addGroup(name: String): Result<Group>

    /**
     * Retrieves all groups the authenticated user is a member of.
     *
     * @return [Result.success] containing a list of [Group] objects,
     *         or [Result.failure] with an exception if retrieval fails.
     */
    suspend fun getAllGroups(): Result<List<Group>>

    /**
     * Retrieves details for a specific group by its unique identifier.
     *
     * @param groupId Unique identifier of the group to retrieve.
     * @return [Result.success] containing the [Group] with full details,
     *         or [Result.failure] with an exception if the group doesn't exist
     *         or user lacks access.
     */
    suspend fun getGroupById(groupId: String): Result<Group>

    /**
     * Joins an existing group using its unique identifier.
     *
     * Adds the authenticated user to the group's member list.
     *
     * @param groupId Unique identifier of the group to join.
     * @return [Result.success] with [Unit] on successful join,
     *         or [Result.failure] with an exception if joining fails.
     */
    suspend fun joinGroup(groupId: String): Result<Unit>
}
