package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.Expense

interface ExpenseRepository {
    suspend fun createExpense(
        groupId: String,
        description: String,
        amount: Double
    ): Result<Expense>

    suspend fun getGroupExpenses(groupId: String): Result<List<Expense>>
}