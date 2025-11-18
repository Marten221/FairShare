package com.example.fairshare.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fairshare.data.remote.ExpenseApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.data.remote.models.CreateExpenseRequest
import com.example.fairshare.domain.model.Expense
import com.example.fairshare.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExpenseRepositoryImpl(
    private val api: ExpenseApi = NetworkModule.expensesApi
) : ExpenseRepository {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val DATE_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun String.toDisplayDate(): String =
        try {
            LocalDateTime.parse(this)          // parses "2025-11-19T13:45:12"
                .toLocalDate()
                .format(DATE_FORMATTER)       // "19.11.2025"
        } catch (e: Exception) {
            // Fallback: just show the date part if parse fails
            this.substringBefore('T')
        }

    @RequiresApi(Build.VERSION_CODES.O)
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
                        ownerEmail = e.owner.email,
                        date = e.timestamp.toDisplayDate()
                    )
                )
            } else {
                Result.failure(Exception("Failed to create expense: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                            ownerEmail = e.owner.email,
                            date = e.timestamp.toDisplayDate()
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