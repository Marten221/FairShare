package com.example.fairshare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fairshare.domain.model.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDAO {

    @Query("SELECT * FROM `GROUP`")
    fun getAllGroups(): Flow<List<Group>>

    @Query("SELECT * FROM `Group` WHERE id = :id")
    fun getGroupById(id: String): Flow<Group>

    @Insert
    fun addGroup(group: Group)
}