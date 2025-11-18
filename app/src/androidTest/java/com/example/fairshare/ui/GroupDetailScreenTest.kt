package com.example.fairshare.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fairshare.R
import com.example.fairshare.domain.model.Group
import com.example.fairshare.domain.model.UserBalance
import com.example.fairshare.domain.model.UserDebts
import com.example.fairshare.ui.screens.GroupDetailScreen
import com.example.fairshare.ui.theme.FairshareTheme
import com.example.fairshare.ui.viewmodels.BalanceState
import com.example.fairshare.ui.viewmodels.DebtsState
import com.example.fairshare.ui.viewmodels.ExpensesState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GroupDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun addExpenseDialog_showsAndSubmitsData() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val addExpenseButtonText = context.getString(R.string.add_expense)
        val descriptionLabelText = context.getString(R.string.add_expense_description_label)
        val amountLabelText = context.getString(R.string.add_expense_amount_label)
        val confirmButtonText = context.getString(R.string.add_expense_cta)

        var submittedDescription: String? = null
        var submittedAmount: Double? = null

        composeRule.setContent {
            FairshareTheme {
                GroupDetailScreen(
                    group = Group(id = "g1", name = "Test group", memberCount = 2),
                    expensesState = ExpensesState.Success(emptyList()),
                    balanceState = BalanceState.Success(
                        UserBalance("x", "x", 0.00,0.00, 0.00)
                        ),
                    debtsState = DebtsState.Success(
                        UserDebts(emptyList(), emptyList())
                        ),
                    onAddExpense = { description, amount ->
                        submittedDescription = description
                        submittedAmount = amount
                    },
                    onBack = {}
                )
            }
        }

        // Click "Add expense" button
        composeRule.onNodeWithText(addExpenseButtonText).performClick()

        // Fill in form
        composeRule.onNodeWithText(descriptionLabelText).performTextInput("Pizza")
        composeRule.onNodeWithText(amountLabelText).performTextInput("12.5")

        // Submit
        composeRule.onNodeWithText(confirmButtonText).performClick()

        // Verify callback got the right values
        assertEquals("Pizza", submittedDescription)
        assertEquals(12.5, submittedAmount!!, 0.001)
    }
}
