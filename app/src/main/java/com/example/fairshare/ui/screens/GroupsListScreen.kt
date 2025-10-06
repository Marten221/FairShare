package com.example.fairshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class Group(val id: String, val name: String, val memberCount: Int) // TODO Move to model package

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsListScreen(onBack: () -> Unit) {
    val groups = listOf(
        Group("1", "Roommates @ Riia 9", 2),
        Group("2", "Italy 2024", 6),
        Group("3", "FairShare Project", 2)
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Groups") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: create group */ }) {
                Icon(Icons.Default.Add, contentDescription = "Create Group")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(groups) { g ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: navigate to group detail */ }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(g.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text("${g.memberCount} members", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}