package com.example.fairshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fairshare.ui.screens.GroupsListScreen
import com.example.fairshare.ui.screens.HomePageScreen
import com.example.fairshare.ui.theme.FairshareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FairshareTheme {
                AppNav()
            }
        }
    }
}

@Composable
private fun AppNav() {
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
            GroupsListScreen(onBack = { nav.popBackStack()})
        }
    }
}