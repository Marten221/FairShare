package com.example.fairshare

import androidx.compose.runtime.Composable
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType})
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            val group = groupId?.let { vm.getGroupById(it)}

            GroupDetailScreen(
                group = group,
                onBack = { nav.popBackStack() }
            )
        }
    }
}