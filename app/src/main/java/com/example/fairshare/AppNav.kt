package com.example.fairshare

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fairshare.ui.screens.GroupsListScreen
import com.example.fairshare.ui.screens.HomePageScreen
import com.example.fairshare.ui.viewmodels.GroupsListViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomePageScreen(
                onSignInSuccess = {
                    nav.navigate("groups")
                }
            )
        }
        composable("groups") {
            val vm: GroupsListViewModel = viewModel()
            GroupsListScreen(vm, onBack = { nav.popBackStack()})
        }
    }
}