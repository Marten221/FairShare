package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.MainApplication
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.repository.GroupsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GroupsListViewModel : ViewModel() {
    private val repo: GroupsRepository = MainApplication.groupsRepository

    fun addGroup(name: String) = viewModelScope.launch(Dispatchers.IO) { repo.addGroup(name) }
    fun getAllGroups(): Flow<List<Group>> = repo.getAllGroups()
    fun getGroupById(id: String): Flow<Group> = repo.getGroupById(id)
}