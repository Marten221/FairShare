package com.example.fairshare.data.repository

import com.example.fairshare.data.local.GroupDatabase
import com.example.fairshare.data.mappers.toDomain
import com.example.fairshare.data.mappers.toEntity
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.repository.GroupsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomGroupsRepository(
    db: GroupDatabase
) : GroupsRepository {

    private val dao = db.getGroupDAO()

    override suspend fun addGroup(name: String) {
        val newGroup = Group(
            id = (10000..99999).random().toString(),
            name = name,
            memberCount = 1
        ).toEntity()
        dao.addGroup(newGroup)
    }

    override fun getAllGroups(): Flow<List<Group>> =
        dao.getAllGroups().map { list -> list.map { it.toDomain() } }

    override fun getGroupById(id: String): Flow<Group> =
        dao.getGroupById(id).map { it.toDomain() }
}