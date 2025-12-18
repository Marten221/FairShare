package com.example.fairshare.data.repository

import com.example.fairshare.data.remote.GroupsApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.data.remote.models.GroupRequest
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.repository.GroupsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Default implementation of [GroupsRepository] using Retrofit [GroupsApi].
 *
 * Handles all group related operations including creation, retrieval, and joining.
 * Maps API response models to domain models and executes network calls
 * on the IO dispatcher for proper threading.
 *
 * @property api Retrofit API interface for group endpoints.
 *               Defaults to [NetworkModule.groupsApi].
 */
class GroupsRepositoryImpl(
    private val api: GroupsApi = NetworkModule.groupsApi
) : GroupsRepository {

    /**
     * Creates a new expense sharing group with the given name.
     *
     * The authenticated user becomes the owner of the newly created group.
     *
     * @param name Display name for the new group.
     * @return [Result.success] containing the created [Group] with server assigned ID,
     *         or [Result.failure] with an exception describing the error.
     */
    override suspend fun addGroup(name: String): Result<Group> = withContext(Dispatchers.IO) {
        try {
            val response = api.createGroup(GroupRequest(name))
            if (response.isSuccessful && response.body() != null) {
                val group = response.body()!!
                Result.success(Group(group.id, group.name, 1000))
            } else {
                Result.failure(Exception("Failed to create group: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves all groups the authenticated user is a member of.
     *
     * Calculates member count as members list size plus one (to include the owner).
     *
     * @return [Result.success] containing a list of [Group] objects,
     *         or [Result.failure] with an exception describing the error.
     */
    override suspend fun getAllGroups(): Result<List<Group>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getGroups()
            if (response.isSuccessful && response.body() != null) {
                val groups = response.body()!!.map {
                    Group(it.id, it.name, it.members.size + 1)
                }
                Result.success(groups)
            } else {
                Result.failure(Exception("Failed to load groups: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves details for a specific group by its unique identifier.
     *
     * Calculates member count as members list size plus one (to include the owner).
     *
     * @param groupId Unique identifier of the group to retrieve.
     * @return [Result.success] containing the [Group] with full details,
     *         or [Result.failure] with an exception describing the error.
     */
    override suspend fun getGroupById(groupId: String): Result<Group> = withContext(Dispatchers.IO) {
        try {
            val response = api.getGroupById(groupId)
            if (response.isSuccessful && response.body() != null) {
                val g = response.body()!!
                Result.success(Group(g.id, g.name, g.members.size + 1))
            } else {
                Result.failure(Exception("Failed to fetch group: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Joins an existing group using its unique identifier.
     *
     * Adds the authenticated user to the group's member list.
     *
     * @param groupId Unique identifier of the group to join.
     * @return [Result.success] with [Unit] on successful join,
     *         or [Result.failure] with an exception describing the error.
     */
    override suspend fun joinGroup(groupId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.joinGroup(groupId)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to add member: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
