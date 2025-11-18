package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.repository.AuthRepositoryImpl
import com.example.fairshare.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repo: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        val res = repo.login(email, password)
        _state.value = res.fold(
            onSuccess = { AuthState.Success(it) },
            onFailure = { AuthState.Error(it.message ?: "Unknown error") }
        )
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        val res = repo.register(email, password)
        _state.value = res.fold(
            onSuccess = { AuthState.Success(it) },
            onFailure = { AuthState.Error(it.message ?: "Unknown error") }
        )
    }

    fun reset() { _state.value = AuthState.Idle }
}