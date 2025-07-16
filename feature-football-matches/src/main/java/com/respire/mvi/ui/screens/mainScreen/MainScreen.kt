package com.respire.mvi.ui.screens.mainScreen

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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.screens.footballMatchesScreen.FootballMatchesScreen
import com.respire.mvi.ui.screens.settingsScreen.SettingsScreen

sealed class TabItem(val title: String, val icon: @Composable () -> Unit) {
    object Matches : TabItem(
        title = "Matches",
        icon = { Icon(Icons.Default.Home, contentDescription = "Matches") }
    )

    object Settings : TabItem(
        title = "Settings",
        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
    )
}

@Composable
fun MainScreen(
    appTheme: ThemeMode,
    onThemeClicked: () -> Unit = {},
    onPrivacyClicked: (url : String) -> Unit = {},
    onMatchClicked: (id: Int) -> Unit = {}
) {
    var currentScreenIndex by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        TabItem.Matches,
        TabItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(TabItem.Matches, TabItem.Settings).forEach { tab ->
                    NavigationBarItem(
                        icon = tab.icon,
                        label = { Text(tab.title) },
                        selected = items[currentScreenIndex] == tab,
                        onClick = { currentScreenIndex = items.indexOf(tab) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding() - 24.dp)) {
            // Matches Screen
            AnimatedVisibility(
                visible = items[currentScreenIndex] is TabItem.Matches,
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
                visible = items[currentScreenIndex] is TabItem.Settings,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SettingsScreen(
                    appTheme = appTheme,
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
    MainScreen(ThemeMode.DARK, {}, {}, {})
}