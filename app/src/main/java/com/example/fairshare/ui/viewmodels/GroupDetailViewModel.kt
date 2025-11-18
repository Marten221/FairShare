package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.repository.BalanceRepositoryImpl
import com.example.fairshare.data.repository.ExpenseRepositoryImpl
import com.example.fairshare.domain.model.Expense
import com.example.fairshare.domain.model.UserBalance
import com.example.fairshare.domain.model.UserDebts
import com.example.fairshare.domain.repository.BalanceRepository
import com.example.fairshare.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ExpensesState {
    data object Idle : ExpensesState()
    data object Loading : ExpensesState()
    data class Success(val expenses: List<Expense>) : ExpensesState()
    data class Error(val message: String) : ExpensesState()
}

sealed class BalanceState {
    data object Idle : BalanceState()
    data object Loading : BalanceState()
    data class Success(val balance: UserBalance) : BalanceState()
    data class Error(val message: String) : BalanceState()
}

sealed class DebtsState {
    data object Idle : DebtsState()
    data object Loading : DebtsState()
    data class Success(val debts: UserDebts) : DebtsState()
    data class Error(val message: String) : DebtsState()
}

class GroupDetailViewModel(
    private val repo: ExpenseRepository = ExpenseRepositoryImpl(),
    private val balanceRepo: BalanceRepository = BalanceRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow<ExpensesState>(ExpensesState.Idle)
    val state: StateFlow<ExpensesState> = _state

    private val _balanceState = MutableStateFlow<BalanceState>(BalanceState.Idle)
    val balanceState: StateFlow<BalanceState> = _balanceState

    private val _debtsState = MutableStateFlow<DebtsState>(DebtsState.Idle)
    val debtsState: StateFlow<DebtsState> = _debtsState

    fun loadExpenses(groupId: String) {
        viewModelScope.launch {
            _state.value = ExpensesState.Loading
            val res = repo.getGroupExpenses(groupId)
            _state.value = res.fold(
                onSuccess = { ExpensesState.Success(it) },
                onFailure = { ExpensesState.Error(it.message ?: "Failed to load expenses") }
            )
        }
    }

    fun loadBalanceAndDebts(groupId: String) {
        viewModelScope.launch {
            _balanceState.value = BalanceState.Loading
            _debtsState.value = DebtsState.Loading

            val balanceRes = balanceRepo.getUserBalance(groupId)
            _balanceState.value = balanceRes.fold(
                onSuccess = { BalanceState.Success(it) },
                onFailure = { BalanceState.Error(it.message ?: "Failed to load balance") }
            )

            val debtsRes = balanceRepo.getUserDebts(groupId)
            _debtsState.value = debtsRes.fold(
                onSuccess = { DebtsState.Success(it) },
                onFailure = { DebtsState.Error(it.message ?: "Failed to load debts") }
            )
        }
    }

    fun addExpense(groupId: String, description: String, amount: Double) {
        viewModelScope.launch {
            val res = repo.createExpense(groupId, description, amount)
            res.onSuccess {
                // Reload expenses and balance after a new expense
                loadExpenses(groupId)
                loadBalanceAndDebts(groupId)
            }
        }
    }
}
