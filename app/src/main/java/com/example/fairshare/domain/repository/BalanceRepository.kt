package com.example.fairshare.domain.repository

import com.example.fairshare.domain.model.UserBalance
import com.example.fairshare.domain.model.UserDebts

interface BalanceRepository {
    suspend fun getUserBalance(groupId: String): Result<UserBalance>
    suspend fun getUserDebts(groupId: String): Result<UserDebts>
}
