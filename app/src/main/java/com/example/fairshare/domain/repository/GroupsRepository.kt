package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupsRepository {
    suspend fun addGroup(name: String)
    fun getAllGroups(): Flow<List<Group>>
    fun getGroupById(id: String): Flow<Group>
}