package com.example.fairshare.domain.model

data class UserBalance(
    val userId: String,
    val userName: String,
    val totalOwes: Double,
    val totalIsOwed: Double,
    val net: Double // totalIsOwed - totalOwes
)

data class Debt(
    val fromUserId: String,
    val fromUserName: String,
    val toUserId: String,
    val toUserName: String,
    val amount: Double
)

data class UserDebts(
    val owe: List<Debt>,   // I owe these people
    val owed: List<Debt>   // These people owe me
)
