package com.example.fairshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.example.fairshare.R
import com.example.fairshare.ui.viewmodels.GroupsListViewModel
import com.example.fairshare.ui.viewmodels.GroupsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsListScreen(
    viewModel: GroupsListViewModel,
    onBack: () -> Unit,
    onGroupClick: (String) -> Unit
) {
    var showAddGroupDialog by rememberSaveable { mutableStateOf(false) }
    var showJoinDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.groups_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = dimensionResource(R.dimen.spacing_l), start = dimensionResource(R.dimen.spacing_xl))
            ) {
                // LEFT: Join Group
                FloatingActionButton(
                    onClick = { showJoinDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = dimensionResource(R.dimen.spacing_l))
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = stringResource(R.string.join_group_fab_content_desc)
                    )
                }
                // RIGHT: Create Group
                FloatingActionButton(
                    onClick = { showAddGroupDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = dimensionResource(R.dimen.spacing_l))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.groups_fab_content_desc)
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val s = state) {
                is GroupsState.Idle,
                is GroupsState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is GroupsState.Error -> {
                    Text(
                        text = "Failed to load groups: ${s.message}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is GroupsState.Success -> {
                    val groups = s.groups
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.spacing_l)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
                    ) {
                        if (groups.isEmpty()) {
                            item {
                                Text(
                                    text = stringResource(R.string.groups_empty),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_l))
                                )
                            }
                        } else {
                            items(groups) { g ->
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onGroupClick(g.id) }
                                ) {
                                    Column(Modifier.padding(dimensionResource(R.dimen.spacing_l))) {
                                        Text(g.name, style = MaterialTheme.typography.titleMedium)
                                        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                                        Text(
                                            pluralStringResource(R.plurals.members_count, g.memberCount, g.memberCount),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showAddGroupDialog) {
        AddGroupDialog(
            onDismiss = { showAddGroupDialog = false },
            onSubmit = {name ->
                viewModel.addGroup(name = name)
                showAddGroupDialog = false
            }
        )

    }
    if (showJoinDialog) {
        JoinGroupDialog(
            onDismiss = { showJoinDialog = false },
            onSubmit = { groupId ->
                viewModel.joinGroup(groupId)
                showJoinDialog = false
            }
        )
    }
}

@Composable
private fun AddGroupDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.create_group_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.create_group_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSubmit(name) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.create_group_cta))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.auth_cancel)) }
        }
    )
}

@Composable
private fun JoinGroupDialog(
    onDismiss: () -> Unit,
    onSubmit: (groupId: String) -> Unit
) {
    var groupId by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.join_group_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))) {
                OutlinedTextField(
                    value = groupId,
                    onValueChange = { groupId = it },
                    label = { Text(stringResource(R.string.join_group_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSubmit(groupId) }, enabled = groupId.isNotBlank()) {
                Text(stringResource(R.string.join_group_cta))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.auth_cancel)) }
        }
    )
}
