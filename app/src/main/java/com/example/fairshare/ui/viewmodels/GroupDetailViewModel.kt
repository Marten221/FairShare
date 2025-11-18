package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.repository.ExpenseRepositoryImpl
import com.example.fairshare.domain.model.Expense
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

class GroupDetailViewModel(
    private val repo: ExpenseRepository = ExpenseRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow<ExpensesState>(ExpensesState.Idle)
    val state: StateFlow<ExpensesState> = _state

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

    fun addExpense(groupId: String, description: String, amount: Double) {
        viewModelScope.launch {
            val res = repo.createExpense(groupId, description, amount)
            res.onSuccess {
                loadExpenses(groupId)
            }
            // if failure, you could update state to Error, but keeping it simple for now
        }
    }
}
