package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.GroupRequest
import com.example.fairshare.data.remote.models.GroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API interface for group management endpoints.
 *
 * Provides methods to create groups, join existing groups, and retrieve
 * group information for the authenticated user.
 */
interface GroupsApi {
    /**
     * Creates a new expense sharing group.
     *
     * The authenticated user becomes the owner of the newly created group.
     *
     * @param body Group creation request containing the group name.
     * @return [Response] containing the created [GroupResponse] with server-assigned ID,
     *         owner information, and initial member list.
     */
    @POST("group")
    suspend fun createGroup(
        @Body body: GroupRequest,
    ): Response<GroupResponse>

    /**
     * Retrieves all groups the authenticated user is a member of.
     *
     * @return [Response] containing a list of [GroupResponse] objects representing
     *         all groups the user belongs to, either as owner or member.
     */
    @GET("groups")
    suspend fun getGroups(): Response<List<GroupResponse>>

    /**
     * Joins an existing group using its unique identifier.
     *
     * Adds the authenticated user to the group's member list.
     *
     * @param groupId Unique identifier of the group to join.
     * @return [Response] with empty body on success,
     *         or an error response if the group doesn't exist or user is already a member.
     */
    @POST("group/{groupId}")
    suspend fun joinGroup(
        @Path("groupId") groupId: String,
    ): Response<Unit>

    /**
     * Retrieves details for a specific group by its ID.
     *
     * @param groupId Unique identifier of the group to retrieve.
     * @return [Response] containing the [GroupResponse] with full group details,
     *         or an error response if the group doesn't exist or user lacks access.
     */
    @GET("group/{groupId}")
    suspend fun getGroupById(
        @Path("groupId") groupId: String,
    ): Response<GroupResponse>
}
