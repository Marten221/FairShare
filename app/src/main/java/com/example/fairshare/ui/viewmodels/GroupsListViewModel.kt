package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.repository.GroupsRepositoryImpl
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.repository.GroupsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class GroupsState {
    data object Idle : GroupsState()
    data object Loading : GroupsState()
    data class Success(val groups: List<Group>) : GroupsState()
    data class Error(val message: String) : GroupsState()
}

class GroupsListViewModel(
    private val repo: GroupsRepository = GroupsRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow<GroupsState>(GroupsState.Idle)
    val state: StateFlow<GroupsState> = _state

    fun addGroup(name: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.addGroup(name)
        loadGroups()
    }

    fun joinGroup(id: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.joinGroup(id)
        loadGroups()
    }

    fun loadGroups() = viewModelScope.launch {
        _state.value = GroupsState.Loading
        val res = repo.getAllGroups()
        _state.value = res.fold(
            onSuccess = { GroupsState.Success(it) },
            onFailure = { GroupsState.Error(it.message ?: "Failed to load groups") }
        )
    }

    fun loadGroupById(id: String) {
        viewModelScope.launch {
            _state.value = GroupsState.Loading
            val res = repo.getGroupById(id)
            _state.value = res.fold(
                onSuccess = { GroupsState.Success(listOf(it)) },
                onFailure = { GroupsState.Error(it.message ?: "Failed to load group") }
            )
        }
    }
}