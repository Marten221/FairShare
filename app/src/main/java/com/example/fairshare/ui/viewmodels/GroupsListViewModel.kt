package com.example.fairshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fairshare.domain.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupsListViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>( emptyList())

    val groups = _groups.asStateFlow()

    init {
        _groups.value = listOf(
            Group(name = "Roommates @ Riia 9", memberCount = 2),
            Group(name = "Italy 2024", memberCount = 6),
            Group(name = "FairShare Project", memberCount = 2)
        )
    }

    fun getGroupById(id: String): Group? = _groups.value.find { it.id == id }
}