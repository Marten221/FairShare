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

/**
 * Default implementation of [ExpenseRepository] using Retrofit [ExpenseApi].
 *
 * Handles expense creation and retrieval, including date formatting for display.
 *
 * @property api Retrofit API interface for expense endpoints.
 *               Defaults to [NetworkModule.expensesApi].
 */
class ExpenseRepositoryImpl(
    private val api: ExpenseApi = NetworkModule.expensesApi
) : ExpenseRepository {

    companion object {
        /**
         * Date formatter for converting timestamps to display format (dd.MM.yyyy).
         */
        @RequiresApi(Build.VERSION_CODES.O)
        private val DATE_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }

    /**
     * Converts an ISO timestamp string to a human-readable date format.
     *
     * Parses timestamps in the format "2025-11-19T13:45:12" and outputs "19.11.2025".
     * Falls back to the date portion before 'T' if parsing fails.
     *
     * @receiver The ISO 8601 timestamp string to convert.
     * @return Formatted date string in dd.MM.yyyy format, or the date portion on parse failure.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun String.toDisplayDate(): String =
        try {
            LocalDateTime.parse(this)
                .toLocalDate()
                .format(DATE_FORMATTER)
        } catch (e: Exception) {
            // Fallback: return the date part before 'T' if parsing fails
            this.substringBefore('T')
        }

    /**
     * Creates a new expense in the specified group.
     *
     * Executes the network call on [Dispatchers.IO] and maps the API response
     * to the domain [Expense] model with formatted date.
     *
     * @param groupId Unique identifier of the group to add the expense to.
     * @param description User provided description of the expense.
     * @param amount Total expense amount to be split among group members.
     * @return [Result.success] containing the created [Expense] with server assigned ID,
     *         or [Result.failure] with an exception describing the error.
     */
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

    /**
     * Retrieves all expenses for a specific group.
     *
     * Executes the network call on [Dispatchers.IO] and maps each API response
     * to domain [Expense] models with formatted dates.
     *
     * @param groupId Unique identifier of the group to get expenses for.
     * @return [Result.success] containing a list of [Expense] objects,
     *         or [Result.failure] with an exception describing the error.
     */
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