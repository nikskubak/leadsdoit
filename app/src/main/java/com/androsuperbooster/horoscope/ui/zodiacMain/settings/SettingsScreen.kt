package com.androsuperbooster.horoscope.ui.zodiacMain.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androsuperbooster.horoscope.R
import com.androsuperbooster.horoscope.ui.base.BaseScreen
import com.androsuperbooster.horoscope.ui.base.BaseUiState
import com.androsuperbooster.horoscope.ui.theme.BaseAppTheme
import com.androsuperbooster.horoscope.ui.themeScreen.ThemeMode

@Composable
fun SettingsScreen(appTheme: ThemeMode, onSelectZodiac: () -> Unit, onAppThemeClick: () -> Unit) {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    BaseScreen(uiState) {
        val content = (uiState as? BaseUiState.ContentState<*>)?.content
        if (content is SettingsUi) {
            SettingsUI(appTheme, content,
                onSelectZodiac,
                onAppThemeClick,
                { isChecked -> viewModel.saveNotification(isChecked) },
                { viewModel.rateApp() })
        }
    }
}

@Composable
fun SettingsUI(
    appTheme: ThemeMode,
    content: SettingsUi,
    onSelectZodiac: () -> Unit,
    onAppThemeClick: () -> Unit,
    onNotificationChanged: (Boolean) -> Unit,
    onRateAppClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Selected Sign
            SettingsItem(
                icon = content.zodiacEntity.animationRes ?: 0,
                title = stringResource(R.string.selected_zodiac),
                value = content.zodiacEntity.name.orEmpty(),
                onSelectZodiac
            )

            HorizontalDivider()

            // Dark Mode
            SettingsItem(
                icon = R.drawable.ic_dark_mode,
                title = stringResource(R.string.app_theme),
                value = when (appTheme) {
                    ThemeMode.LIGHT -> stringResource(R.string.light)
                    ThemeMode.DARK -> stringResource(R.string.dark)
                    ThemeMode.SYSTEM -> stringResource(R.string.system)
                },
                onAppThemeClick
            )

            HorizontalDivider()

            // Notifications
            SettingsItem(
                icon = R.drawable.ic_notifications,
                title = stringResource(R.string.notifications),
                value = if (content.isNotificationEnabled) "Enabled" else "Disabled",
                {},
                content.isNotificationEnabled,
                { isChecked -> onNotificationChanged(isChecked) }
            )

            HorizontalDivider()

            // Rate App
            SettingsItem(
                icon = R.drawable.ic_star,
                title = stringResource(R.string.rate_app),
                value = "★★★★★",
                onRateAppClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    BaseAppTheme(darkTheme = false, dynamicColor = false) {
        SettingsScreen(ThemeMode.SYSTEM, {}, {})
    }
}