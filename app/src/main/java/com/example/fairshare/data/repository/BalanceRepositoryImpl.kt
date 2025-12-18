package com.example.fairshare.data.repository

import com.example.fairshare.data.remote.BalanceApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.domain.model.Debt
import com.example.fairshare.domain.model.UserBalance
import com.example.fairshare.domain.model.UserDebts
import com.example.fairshare.domain.repository.BalanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Default implementation of [BalanceRepository] using Retrofit [BalanceApi].
 *
 * Maps API response models to domain models and executes network calls
 * on the IO dispatcher for proper threading.
 *
 * @property api Retrofit API interface for balance endpoints.
 *               Defaults to [NetworkModule.balanceApi].
 */
class BalanceRepositoryImpl(
    private val api: BalanceApi = NetworkModule.balanceApi
) : BalanceRepository {

    /**
     * Retrieves the current user's balance summary for a specific group.
     *
     * Executes the network call on [Dispatchers.IO] and maps the API response
     * to the domain [UserBalance] model.
     *
     * @param groupId Unique identifier of the group to get balance for.
     * @return [Result.success] containing [UserBalance] with balance details,
     *         or [Result.failure] with an exception describing the error.
     */
    override suspend fun getUserBalance(groupId: String): Result<UserBalance> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getUserBalance(groupId)
                if (response.isSuccessful && response.body() != null) {
                    val b = response.body()!!
                    Result.success(
                        UserBalance(
                            userId = b.userId,
                            userName = b.userName,
                            totalOwes = b.totalOwes,
                            totalIsOwed = b.totalIsOwed,
                            net = b.net
                        )
                    )
                } else {
                    Result.failure(Exception("Failed to load balance: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Retrieves detailed debt information for the current user in a specific group.
     *
     * Executes the network call on [Dispatchers.IO] and maps the API response
     * to the domain [UserDebts] model with nested [Debt] lists.
     *
     * @param groupId Unique identifier of the group to get debts for.
     * @return [Result.success] containing [UserDebts] with detailed debt breakdowns,
     *         or [Result.failure] with an exception describing the error.
     */
    override suspend fun getUserDebts(groupId: String): Result<UserDebts> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getUserDebts(groupId)
                if (response.isSuccessful && response.body() != null) {
                    val d = response.body()!!
                    Result.success(
                        UserDebts(
                            owe = d.owe.map {
                                Debt(
                                    fromUserId = it.fromUserId,
                                    fromUserName = it.fromUserName,
                                    toUserId = it.toUserId,
                                    toUserName = it.toUserName,
                                    amount = it.amount
                                )
                            },
                            owed = d.owed.map {
                                Debt(
                                    fromUserId = it.fromUserId,
                                    fromUserName = it.fromUserName,
                                    toUserId = it.toUserId,
                                    toUserName = it.toUserName,
                                    amount = it.amount
                                )
                            }
                        )
                    )
                } else {
                    Result.failure(Exception("Failed to load debts: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
