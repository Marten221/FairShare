package com.example.fairshare.data.repository

import com.example.fairshare.data.remote.BalanceApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.domain.model.Debt
import com.example.fairshare.domain.model.UserBalance
import com.example.fairshare.domain.model.UserDebts
import com.example.fairshare.domain.repository.BalanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BalanceRepositoryImpl(
    private val api: BalanceApi = NetworkModule.balanceApi
) : BalanceRepository {

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
