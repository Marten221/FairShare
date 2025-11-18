package com.example.fairshare.data.repository

import com.example.fairshare.data.remote.ExpenseApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.data.remote.models.CreateExpenseRequest
import com.example.fairshare.domain.model.Expense
import com.example.fairshare.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val api: ExpenseApi = NetworkModule.expensesApi
) : ExpenseRepository {

    override suspend fun createExpense(
        groupId: String,
        description: String,
        amount: Double
    ): Result<Expense> = withContext(Dispatchers.IO) {
        try {
            val response = api.createExpense(
                groupId = groupId,
                body = CreateExpenseRequest(description, amount)
            )

            if (response.isSuccessful && response.body() != null) {
                val e = response.body()!!
                Result.success(
                    Expense(
                        id = e.id,
                        description = e.description,
                        amount = e.amount,
                        ownerEmail = e.owner.email
                    )
                )
            } else {
                Result.failure(Exception("Failed to create expense: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupExpenses(groupId: String): Result<List<Expense>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getGroupExpenses(groupId)
                if (response.isSuccessful && response.body() != null) {
                    val expenses = response.body()!!.map { e ->
                        Expense(
                            id = e.id,
                            description = e.description,
                            amount = e.amount,
                            ownerEmail = e.owner.email
                        )
                    }
                    Result.success(expenses)
                } else {
                    Result.failure(Exception("Failed to load expenses: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}