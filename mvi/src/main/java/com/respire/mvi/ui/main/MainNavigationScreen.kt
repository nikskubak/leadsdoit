package com.respire.mvi.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.respire.mvi.ui.footballMatches.FootballMatchesScreen
import com.respire.mvi.ui.settings.SettingsScreen

sealed class TabItem(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Matches : TabItem(
        route = "matches",
        title = "Matches",
        icon = { Icon(Icons.Default.Home, contentDescription = "Matches") }
    )
    
    object Settings : TabItem(
        route = "settings",
        title = "Settings",
        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
    )
}

@Composable
fun MainNavigationScreen(
    onThemeClicked: () -> Unit = {},
    onPrivacyClicked: () -> Unit = {},
    onMatchClicked: (id: Int) -> Unit = {}
) {
    var selectedTab: TabItem by remember { mutableStateOf(TabItem.Matches) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(TabItem.Matches, TabItem.Settings).forEach { tab ->
                    NavigationBarItem(
                        icon = tab.icon,
                        label = { Text(tab.title) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding() - 24.dp)) {
            // Matches Screen
            AnimatedVisibility(
                visible = selectedTab is TabItem.Matches,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FootballMatchesScreen(
                    onItemSelected = onMatchClicked,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Settings Screen
            AnimatedVisibility(
                visible = selectedTab is TabItem.Settings,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SettingsScreen(
                    onThemeClicked = onThemeClicked,
                    onPrivacyClicked = onPrivacyClicked,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainNavigationScreenPreview() {
    MainNavigationScreen({}, {})
}