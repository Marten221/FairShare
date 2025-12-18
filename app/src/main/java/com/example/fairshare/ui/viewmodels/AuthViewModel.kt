package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.repository.AuthRepositoryImpl
import com.example.fairshare.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Sealed class representing the possible states of authentication operations.
 */
sealed class AuthState {
    /** Initial state before any authentication operation is triggered. */
    data object Idle : AuthState()

    /** Authentication operation is in progress. */
    data object Loading : AuthState()

    /**
     * Authentication operation completed successfully.
     *
     * @property message Success message from the server.
     */
    data class Success(val message: String) : AuthState()

    /**
     * Authentication operation failed.
     *
     * @property message User friendly error message describing the failure.
     */
    data class Error(val message: String) : AuthState()
}

/**
 * ViewModel managing authentication state and operations.
 *
 * Handles user login and registration, exposing the current authentication state
 * as a [StateFlow] for UI observation.
 *
 * @property repo Repository for performing authentication operations.
 *                Defaults to [AuthRepositoryImpl].
 */
class AuthViewModel(
    private val repo: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)

    /**
     * Observable authentication state for UI binding.
     *
     * Emits [AuthState] updates reflecting the current status of authentication operations.
     */
    val state: StateFlow<AuthState> = _state

    /**
     * Attempts to log in with the provided credentials.
     *
     * Updates [state] to [AuthState.Loading] during the operation,
     * then to [AuthState.Success] or [AuthState.Error] based on the result.
     *
     * @param email User's email address.
     * @param password User's password.
     */
    fun login(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        val res = repo.login(email, password)
        _state.value = res.fold(
            onSuccess = { AuthState.Success(it) },
            onFailure = { AuthState.Error(it.message ?: "Unknown error") }
        )
    }

    /**
     * Attempts to register a new user account.
     *
     * Updates [state] to [AuthState.Loading] during the operation,
     * then to [AuthState.Success] or [AuthState.Error] based on the result.
     *
     * @param email User's email address (must be unique).
     * @param password User's chosen password.
     */
    fun register(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        val res = repo.register(email, password)
        _state.value = res.fold(
            onSuccess = { AuthState.Success(it) },
            onFailure = { AuthState.Error(it.message ?: "Unknown error") }
        )
    }

    /**
     * Resets the authentication state to [AuthState.Idle].
     *
     * Should be called after handling success or error states to prepare
     * for subsequent authentication attempts.
     */
    fun reset() { _state.value = AuthState.Idle }
}