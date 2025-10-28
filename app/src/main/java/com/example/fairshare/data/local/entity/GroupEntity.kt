package com.example.fairshare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val memberCount: Int
)