package com.example.fairshare.data.repository

import com.example.fairshare.data.remote.GroupsApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.data.remote.models.GroupRequest
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.repository.GroupsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupsRepositoryImpl(
    private val api: GroupsApi = NetworkModule.groupsApi
) : GroupsRepository {

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
