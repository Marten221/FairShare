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

/**
 * Sealed class representing the possible states of groups list loading.
 */
sealed class GroupsState {
    /** Initial state before any loading operation is triggered. */
    data object Idle : GroupsState()

    /** Groups are currently being loaded from the server. */
    data object Loading : GroupsState()

    /**
     * Groups loaded successfully.
     *
     * @property groups List of groups the user belongs to.
     */
    data class Success(val groups: List<Group>) : GroupsState()

    /**
     * Failed to load groups.
     *
     * @property message Error message describing the failure.
     */
    data class Error(val message: String) : GroupsState()
}

/**
 * ViewModel managing the groups list state and operations.
 *
 * Handles loading groups, creating new groups, and joining existing groups.
 * Exposes the current groups state as a [StateFlow] for UI observation.
 *
 * @property repo Repository for group operations. Defaults to [GroupsRepositoryImpl].
 */
class GroupsListViewModel(
    private val repo: GroupsRepository = GroupsRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow<GroupsState>(GroupsState.Idle)

    /**
     * Observable groups state for UI binding.
     *
     * Emits [GroupsState] updates reflecting the current status of group operations.
     */
    val state: StateFlow<GroupsState> = _state

    /**
     * Creates a new expense sharing group with the given name.
     *
     * Executes on [Dispatchers.IO] and automatically reloads the groups list
     * after successful creation.
     *
     * @param name Display name for the new group.
     */
    fun addGroup(name: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.addGroup(name)
        loadGroups()
    }

    /**
     * Joins an existing group using its unique identifier.
     *
     * Executes on [Dispatchers.IO] and automatically reloads the groups list
     * after successful join.
     *
     * @param id Unique identifier of the group to join.
     */
    fun joinGroup(id: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.joinGroup(id)
        loadGroups()
    }

    /**
     * Loads all groups the current user belongs to.
     *
     * Updates [state] to [GroupsState.Loading] during the operation,
     * then to [GroupsState.Success] or [GroupsState.Error] based on the result.
     */
    fun loadGroups() = viewModelScope.launch {
        _state.value = GroupsState.Loading
        val res = repo.getAllGroups()
        _state.value = res.fold(
            onSuccess = { GroupsState.Success(it) },
            onFailure = { GroupsState.Error(it.message ?: "Failed to load groups") }
        )
    }

    /**
     * Loads a specific group by its unique identifier.
     *
     * Updates [state] to [GroupsState.Loading] during the operation,
     * then to [GroupsState.Success] (with a single-item list) or [GroupsState.Error].
     *
     * @param id Unique identifier of the group to load.
     */
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