package com.example.fairshare.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey(autoGenerate = false)
    val id: String = (10000..99999).random().toString(),
    val name: String,
    val memberCount: Int
)
