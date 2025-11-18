package com.example.fairshare.data.remote.models


data class ExpenseResponse(
    val id: String,
    val description: String,
    val amount: Double,
    val timestamp: String,
    val owner: User
)

data class CreateExpenseRequest(
    val description: String,
    val amount: Double
)