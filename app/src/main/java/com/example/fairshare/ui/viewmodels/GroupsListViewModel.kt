package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.MainApplication
import com.example.fairshare.domain.model.Group
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GroupsListViewModel : ViewModel() {
    val groupDAO = MainApplication.groupDatabase.getGroupDAO()
    fun addGroup(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            groupDAO.addGroup(Group(name = name))
        }
    }

    fun getAllGroups(): Flow<List<Group>> {
        return groupDAO.getAllGroups()
    }

    fun getGroupById(id: String): Flow<Group> {
        return groupDAO.getGroupById(id)
    }
}