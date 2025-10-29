package com.example.fairshare.data.remote.models

data class User(
    val id: String,
    val email: String
)

data class GroupRequest(
    val name: String
)

data class GroupResponse(
    val id: String,
    val name: String,
    val owner: User,
    val members: List<User>
)