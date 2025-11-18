package com.example.fairshare.data.remote.models

data class UserBalanceResponse(
    val userId: String,
    val userName: String,
    val totalOwes: Double,
    val totalIsOwed: Double,
    val net: Double
)

data class DebtResponse(
    val fromUserId: String,
    val fromUserName: String,
    val toUserId: String,
    val toUserName: String,
    val amount: Double
)

data class UserDebtsResponse(
    val owe: List<DebtResponse>,   // I owe these people
    val owed: List<DebtResponse>   // These people owe me
)
