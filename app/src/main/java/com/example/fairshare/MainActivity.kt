package com.example.fairshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.fairshare.ui.theme.FairshareTheme

/**
 * Main entry point activity for the FairShare application.
 *
 * This activity serves as the single activity host for the Compose-based UI.
 * It handles:
 * - Splash screen display during app initialization
 * - Theme state management (dark/light mode toggle)
 * - Root navigation setup via [AppNav]
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     *
     * Sets up the splash screen and initializes
     * the Compose UI with theme support and navigation.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state,
     *                           or null if this is a fresh start.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                val systemDark = isSystemInDarkTheme()
                var isDarkTheme by rememberSaveable { mutableStateOf(systemDark) }

                FairshareTheme(darkTheme = isDarkTheme) {
                    AppNav(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}