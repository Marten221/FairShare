package com.example.fairshare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.fairshare.R
import com.example.fairshare.domain.model.Group

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    group: Group?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        group?.name ?: stringResource(R.string.group_fallback_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.nav_back))
                    }
                }
            )
        }
    ) { padding ->
        if (group == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(dimensionResource(R.dimen.spacing_l)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
            ) {
                Text(stringResource(R.string.group_not_found_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.group_not_found_body), style = MaterialTheme.typography.bodyMedium)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.spacing_l)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
        ) {
            Text(pluralStringResource(R.plurals.members_count, group.memberCount, group.memberCount), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.group_id_label, group.id), style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_md)))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Expenses placeholder
            Text(stringResource(R.string.expenses_title), style = MaterialTheme.typography.titleMedium)
            Text(
                stringResource(R.string.expenses_empty),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            // TODO replace with LazyColumn of expenses
        }
    }
}