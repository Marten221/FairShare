package com.example.fairshare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.fairshare.R
import com.example.fairshare.domain.model.Expense
import com.example.fairshare.domain.model.Group
import com.example.fairshare.ui.viewmodels.ExpensesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    group: Group?,
    expensesState: ExpensesState,
    onAddExpense: (description: String, amount: Double) -> Unit,
    onBack: () -> Unit
) {
    var showAddExpenseDialog by rememberSaveable { mutableStateOf(false) }

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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
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
                Text(
                    stringResource(R.string.group_not_found_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    stringResource(R.string.group_not_found_body),
                    style = MaterialTheme.typography.bodyMedium
                )
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
            Text(
                pluralStringResource(
                    R.plurals.members_count,
                    group.memberCount,
                    group.memberCount
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(R.string.group_id_label, group.id),
                style = MaterialTheme.typography.bodySmall,
                fontSize = dimensionResource(R.dimen.id_text).value.sp,
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_md)))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.expenses_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = { showAddExpenseDialog = true }) {
                    Text(stringResource(R.string.add_expense))
                }
            }

            when (val s = expensesState) {
                is ExpensesState.Idle,
                is ExpensesState.Loading -> {
                    Text(
                        text = stringResource(R.string.expenses_loading),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is ExpensesState.Error -> {
                    Text(
                        text = stringResource(R.string.expenses_error),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is ExpensesState.Success -> {
                    if (s.expenses.isEmpty()) {
                        Text(
                            stringResource(R.string.expenses_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        ExpensesList(expenses = s.expenses)
                    }
                }
            }
        }
    }

    if (showAddExpenseDialog) {
        AddExpenseDialog(
            onDismiss = { showAddExpenseDialog = false },
            onSubmit = { description, amount ->
                onAddExpense(description, amount)
                showAddExpenseDialog = false
            }
        )
    }
}

@Composable
private fun ExpensesList(expenses: List<Expense>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
    ) {
        items(expenses) { expense ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_sm))
            ) {
                Text(
                    expense.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = "€${"%.2f".format(expense.amount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.expense_owner_label, expense.ownerEmail),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_sm)))
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}

@Composable
private fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSubmit: (description: String, amount: Double) -> Unit
) {
    var description by rememberSaveable { mutableStateOf("") }
    var amountText by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_expense_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.add_expense_description_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text(stringResource(R.string.add_expense_amount_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    onSubmit(description, amount)
                },
                enabled = description.isNotBlank() && amountText.toDoubleOrNull() != null
            ) {
                Text(stringResource(R.string.add_expense_cta))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.auth_cancel)) }
        }
    )
}
