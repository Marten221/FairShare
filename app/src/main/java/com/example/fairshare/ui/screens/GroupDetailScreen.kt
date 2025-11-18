package com.example.fairshare.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ElevatedCard
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
import com.example.fairshare.domain.model.Debt
import com.example.fairshare.domain.model.Expense
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.model.UserDebts
import com.example.fairshare.ui.viewmodels.BalanceState
import com.example.fairshare.ui.viewmodels.DebtsState
import com.example.fairshare.ui.viewmodels.ExpensesState
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    group: Group?,
    expensesState: ExpensesState,
    balanceState: BalanceState,
    debtsState: DebtsState,
    onAddExpense: (description: String, amount: Double) -> Unit,
    onBack: () -> Unit
) {
    var showAddExpenseDialog by rememberSaveable { mutableStateOf(false) }
    var showDebtsDialog by rememberSaveable { mutableStateOf(false) }

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

            BalanceSummary(
                balanceState = balanceState,
                onClick = { showDebtsDialog = true }
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

    if (showDebtsDialog) {
        DebtsDialog(
            debtsState = debtsState,
            onDismiss = { showDebtsDialog = false }
        )
    }
}

@Composable
private fun BalanceSummary(
    balanceState: BalanceState,
    onClick: () -> Unit
) {
    when (val s = balanceState) {
        is BalanceState.Idle,
        is BalanceState.Loading -> {
            Text(
                text = "Calculating your balance...",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        is BalanceState.Error -> {
            Text(
                text = "Failed to load balance: ${s.message}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        is BalanceState.Success -> {
            val net = s.balance.net
            val absNet = abs(net)
            val mainText = when {
                net > 0.01 -> "You are owed €${"%.2f".format(absNet)} in this group"
                net < -0.01 -> "You owe €${"%.2f".format(absNet)} in this group"
                else -> "You are settled up in this group"
            }

            val subText = if (absNet > 0.01) {
                "Tap to see who you owe and who owes you"
            } else {
                "No outstanding balances with other members"
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = absNet > 0.01) { onClick() }
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_md)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
                ) {
                    Text(
                        text = "Your balance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = mainText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DebtsDialog(
    debtsState: DebtsState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Who owes who") },
        text = {
            when (val s = debtsState) {
                is DebtsState.Idle,
                is DebtsState.Loading -> {
                    Text("Loading details…")
                }

                is DebtsState.Error -> {
                    Text("Failed to load debts: ${s.message}")
                }

                is DebtsState.Success -> {
                    DebtsDialogBody(debts = s.debts)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun DebtsDialogBody(debts: UserDebts) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
    ) {
        if (debts.owe.isNotEmpty()) {
            Text(
                text = "You owe",
                style = MaterialTheme.typography.titleSmall
            )
            debts.owe.forEach { d: Debt ->
                Text(
                    text = "• ${d.toUserName}: €${"%.2f".format(d.amount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (debts.owed.isNotEmpty()) {
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_sm)))
            Text(
                text = "Owe you",
                style = MaterialTheme.typography.titleSmall
            )
            debts.owed.forEach { d: Debt ->
                Text(
                    text = "• ${d.fromUserName}: €${"%.2f".format(d.amount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (debts.owe.isEmpty() && debts.owed.isEmpty()) {
            Text(
                text = "You have no outstanding balances with anyone in this group.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "€${"%.2f".format(expense.amount)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = expense.date,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

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
