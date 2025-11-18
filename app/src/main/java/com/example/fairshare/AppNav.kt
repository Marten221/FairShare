package com.example.fairshare

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fairshare.ui.screens.GroupDetailScreen
import com.example.fairshare.ui.screens.GroupsListScreen
import com.example.fairshare.ui.screens.HomePageScreen
import com.example.fairshare.ui.viewmodels.GroupsListViewModel
import com.example.fairshare.ui.viewmodels.GroupsState
import com.example.fairshare.ui.viewmodels.GroupDetailViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val vm: GroupsListViewModel = viewModel()

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomePageScreen(
                onSignInSuccess = {
                    nav.navigate("groups")
                }
            )
        }
        composable("groups") {
            GroupsListScreen(
                viewModel = vm,
                onBack = { nav.popBackStack()},
                onGroupClick = { groupId -> nav.navigate("group/$groupId")}
            )
        }
        composable(
            route = "group/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val groupsVm: GroupsListViewModel = viewModel()
            val detailVm: GroupDetailViewModel = viewModel()

            val groupsState by groupsVm.state.collectAsState()
            val expensesState by detailVm.state.collectAsState()

            LaunchedEffect(groupId) {
                groupsVm.loadGroupById(groupId)
                detailVm.loadExpenses(groupId)
            }

            when (groupsState) {
                is GroupsState.Success -> {
                    val group = (groupsState as GroupsState.Success).groups.firstOrNull()
                    GroupDetailScreen(
                        group = group,
                        expensesState = expensesState,
                        onAddExpense = { description, amount ->
                            detailVm.addExpense(groupId, description, amount)
                        },
                        onBack = { nav.popBackStack() }
                    )
                }
                else -> Unit
            }
        }


    }
}