package com.example.fairshare.domain.model

data class Group(
    val id: String = (10000..99999).random().toString(),
    val name: String,
    val memberCount: Int
)
