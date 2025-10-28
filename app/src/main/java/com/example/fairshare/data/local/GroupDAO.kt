package com.example.fairshare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fairshare.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDAO {

    @Query("SELECT * FROM `groups`")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `groups` WHERE id = :id")
    fun getGroupById(id: String): Flow<GroupEntity>

    @Insert
    suspend fun addGroup(group: GroupEntity)
}