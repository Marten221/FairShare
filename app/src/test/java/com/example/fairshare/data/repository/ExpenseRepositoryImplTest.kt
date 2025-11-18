package com.example.fairshare.data.repository

import android.os.Build
import com.example.fairshare.data.remote.ExpenseApi
import com.example.fairshare.data.remote.models.CreateExpenseRequest
import com.example.fairshare.data.remote.models.ExpenseResponse
import com.example.fairshare.data.remote.models.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ExpenseRepositoryImplTest {

    /**
     * Simple fake API that returns one expense with a known timestamp.
     */
    private class FakeExpenseApi : ExpenseApi {
        override suspend fun createExpense(
            groupId: String,
            body: CreateExpenseRequest
        ): Response<ExpenseResponse> {
            // Not used in this test
            return Response.success(
                ExpenseResponse(
                    id = "dummy",
                    description = body.description,
                    amount = body.amount,
                    timestamp = "2025-11-19T13:45:12",
                    owner = User(id = "u1", email = "test@example.com")
                )
            )
        }

        override suspend fun getGroupExpenses(
            groupId: String
        ): Response<List<ExpenseResponse>> {
            val expense = ExpenseResponse(
                id = "1",
                description = "Test expense",
                amount = 10.0,
                timestamp = "2025-11-19T13:45:12",
                owner = User(id = "u1", email = "test@example.com")
            )
            return Response.success(listOf(expense))
        }
    }

    @Test
    @androidx.test.filters.SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    fun getGroupExpenses_formatsTimestampToDisplayDate() = runBlocking {
        // Arrange
        val repo = ExpenseRepositoryImpl(api = FakeExpenseApi())

        // Act
        val result = repo.getGroupExpenses("group1")

        // Assert
        assertTrue(result.isSuccess)
        val expenses = result.getOrNull()
        requireNotNull(expenses)
        assertEquals(1, expenses.size)
        // 2025-11-19T13:45:12 -> "19.11.2025"
        assertEquals("19.11.2025", expenses[0].date)
    }
}
