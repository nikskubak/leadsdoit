package com.respire.mvi.ui.base

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.respire.mvi.ui.base.ui.theme.BaseAppTheme
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.main.MainNavigationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by remember { mutableStateOf(ThemeMode.SYSTEM) }
            BaseAppTheme(
                darkTheme = when (appTheme) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                MainNavigationScreen(
                    onMatchSelected = { matchId ->
                        Log.e("onMatchSelected", "Match ID: $matchId")
                    },
                    onSettingsAction = { action ->
                        Log.e("onSettingsAction", "Action: $action")
                    }
                )
            }
        }
    }
}