package com.example.fairshare.domain.model

data class Expense(
    val id: String,
    val description: String,
    val amount: Double,
    val ownerEmail: String,
    val date: String
)