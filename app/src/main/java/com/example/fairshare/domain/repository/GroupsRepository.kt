package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.Group

interface GroupsRepository {
    suspend fun addGroup(name: String): Result<Group>
    suspend fun getAllGroups(): Result<List<Group>>
    suspend fun getGroupById(groupId: String): Result<Group>
    suspend fun joinGroup(groupId: String): Result<Unit>
}