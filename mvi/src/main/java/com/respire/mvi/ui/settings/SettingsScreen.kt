package com.respire.mvi.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.respire.mvi.R
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.settings.state.SettingsEffect
import com.respire.mvi.ui.settings.state.SettingsEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    appTheme : ThemeMode,
    onThemeClicked: () -> Unit = {},
    onPrivacyClicked: (url : String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsState()

    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SettingsEffect.NavigateToThemes -> {
                    onThemeClicked()
                }

                is SettingsEffect.NavigateToPrivacyPolicy -> {
                    onPrivacyClicked(effect.url.orEmpty())
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                title = { Text("Settings") },
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {

                // Dark Mode
                SettingsItem(
                    icon = R.drawable.ic_dark_mode,
                    title = stringResource(R.string.app_theme),
                    value = when (appTheme) {
                        ThemeMode.LIGHT -> stringResource(R.string.light)
                        ThemeMode.DARK -> stringResource(R.string.dark)
                        ThemeMode.SYSTEM -> stringResource(R.string.system)
                    },
                    onThemeClicked
                )

                HorizontalDivider()

                // Notifications
                SettingsItem(
                    icon = R.drawable.ic_notifications,
                    title = stringResource(R.string.notifications),
                    value = if (state.settingsEntity?.isNotificationEnabled == true) "Enabled" else "Disabled",
                    {},
                    state.settingsEntity?.isNotificationEnabled == true,
                    { isChecked -> viewModel.onEvent(SettingsEvent.OnNotificationChanged(isChecked)) }
                )

                HorizontalDivider()

                // Notifications
                SettingsItem(
                    icon = R.drawable.ic_notifications,
                    title = stringResource(R.string.privacy_policy),
                    value = "",
                    onClick = {
                        viewModel.onEvent(SettingsEvent.OnPrivacyClicked)
                    }
                )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(ThemeMode.DARK)
}


