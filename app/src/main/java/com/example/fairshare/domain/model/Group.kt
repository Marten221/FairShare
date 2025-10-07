package com.example.fairshare.domain.model

import java.util.UUID

data class Group(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val memberCount: Int
)
