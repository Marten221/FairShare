package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.GroupRequest
import com.example.fairshare.data.remote.models.GroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupsApi {
    @POST("group")
    suspend fun createGroup(@Body body: GroupRequest): Response<GroupResponse>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupResponse>>

    @POST("group/{groupId}")
    suspend fun joinGroup(@Path("groupId") groupId: String): Response<Unit>

    @GET("group/{groupId}")
    suspend fun getGroupById(@Path("groupId") groupId: String): Response<GroupResponse>
}