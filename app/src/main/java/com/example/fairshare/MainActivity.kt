package com.example.fairshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fairshare.ui.screens.HomePageScreen
import com.example.fairshare.ui.theme.FairshareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FairshareTheme {
                HomePageScreen()
            }
        }
    }
}
