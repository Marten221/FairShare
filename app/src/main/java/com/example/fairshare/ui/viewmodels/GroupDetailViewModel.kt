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

/**
 * Sealed class representing the possible states of expense list loading.
 */
sealed class ExpensesState {
    /** Initial state before any loading operation is triggered. */
    data object Idle : ExpensesState()

    /** Expenses are currently being loaded from the server. */
    data object Loading : ExpensesState()

    /**
     * Expenses loaded successfully.
     *
     * @property expenses List of expenses in the group.
     */
    data class Success(
        val expenses: List<Expense>,
    ) : ExpensesState()

    /**
     * Failed to load expenses.
     *
     * @property message Error message describing the failure.
     */
    data class Error(
        val message: String,
    ) : ExpensesState()
}

/**
 * Sealed class representing the possible states of balance loading.
 */
sealed class BalanceState {
    /** Initial state before any loading operation is triggered. */
    data object Idle : BalanceState()

    /** Balance is currently being calculated/loaded from the server. */
    data object Loading : BalanceState()

    /**
     * Balance loaded successfully.
     *
     * @property balance User's balance summary in the group.
     */
    data class Success(
        val balance: UserBalance,
    ) : BalanceState()

    /**
     * Failed to load balance.
     *
     * @property message Error message describing the failure.
     */
    data class Error(
        val message: String,
    ) : BalanceState()
}

/**
 * Sealed class representing the possible states of debt details loading.
 */
sealed class DebtsState {
    /** Initial state before any loading operation is triggered. */
    data object Idle : DebtsState()

    /** Debt details are currently being loaded from the server. */
    data object Loading : DebtsState()

    /**
     * Debts loaded successfully.
     *
     * @property debts Detailed debt breakdown for the user.
     */
    data class Success(
        val debts: UserDebts,
    ) : DebtsState()

    /**
     * Failed to load debts.
     *
     * @property message Error message describing the failure.
     */
    data class Error(
        val message: String,
    ) : DebtsState()
}

/**
 * ViewModel managing the state for a group detail screen.
 *
 * Handles loading and displaying expenses, balance summary, and detailed
 * debt information for a specific group. Also manages adding new expenses.
 *
 * @property repo Repository for expense operations. Defaults to [ExpenseRepositoryImpl].
 * @property balanceRepo Repository for balance operations. Defaults to [BalanceRepositoryImpl].
 */
class GroupDetailViewModel(
    private val repo: ExpenseRepository = ExpenseRepositoryImpl(),
    private val balanceRepo: BalanceRepository = BalanceRepositoryImpl(),
) : ViewModel() {
    private val _state = MutableStateFlow<ExpensesState>(ExpensesState.Idle)

    /**
     * Observable expenses state for UI binding.
     *
     * Emits [ExpensesState] updates reflecting the current status of expense loading.
     */
    val state: StateFlow<ExpensesState> = _state

    private val _balanceState = MutableStateFlow<BalanceState>(BalanceState.Idle)

    /**
     * Observable balance state for UI binding.
     *
     * Emits [BalanceState] updates reflecting the current status of balance loading.
     */
    val balanceState: StateFlow<BalanceState> = _balanceState

    private val _debtsState = MutableStateFlow<DebtsState>(DebtsState.Idle)

    /**
     * Observable debts state for UI binding.
     *
     * Emits [DebtsState] updates reflecting the current status of debt details loading.
     */
    val debtsState: StateFlow<DebtsState> = _debtsState

    /**
     * Loads all expenses for the specified group.
     *
     * Updates [state] to [ExpensesState.Loading] during the operation,
     * then to [ExpensesState.Success] or [ExpensesState.Error] based on the result.
     *
     * @param groupId Unique identifier of the group to load expenses for.
     */
    fun loadExpenses(groupId: String) {
        viewModelScope.launch {
            _state.value = ExpensesState.Loading
            val res = repo.getGroupExpenses(groupId)
            _state.value =
                res.fold(
                    onSuccess = { ExpensesState.Success(it) },
                    onFailure = { ExpensesState.Error(it.message ?: "Failed to load expenses") },
                )
        }
    }

    /**
     * Loads both balance summary and detailed debts for the specified group.
     *
     * Updates [balanceState] and [debtsState] to Loading during the operation,
     * then to Success or Error states based on the respective results.
     *
     * @param groupId Unique identifier of the group to load balance data for.
     */
    fun loadBalanceAndDebts(groupId: String) {
        viewModelScope.launch {
            _balanceState.value = BalanceState.Loading
            _debtsState.value = DebtsState.Loading

            val balanceRes = balanceRepo.getUserBalance(groupId)
            _balanceState.value =
                balanceRes.fold(
                    onSuccess = { BalanceState.Success(it) },
                    onFailure = { BalanceState.Error(it.message ?: "Failed to load balance") },
                )

            val debtsRes = balanceRepo.getUserDebts(groupId)
            _debtsState.value =
                debtsRes.fold(
                    onSuccess = { DebtsState.Success(it) },
                    onFailure = { DebtsState.Error(it.message ?: "Failed to load debts") },
                )
        }
    }

    /**
     * Creates a new expense in the specified group.
     *
     * On success, automatically reloads both the expenses list and balance data
     * to reflect the changes.
     *
     * @param groupId Unique identifier of the group to add the expense to.
     * @param description User-provided description of the expense.
     * @param amount Total expense amount to be split among group members.
     */
    fun addExpense(
        groupId: String,
        description: String,
        amount: Double,
    ) {
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
