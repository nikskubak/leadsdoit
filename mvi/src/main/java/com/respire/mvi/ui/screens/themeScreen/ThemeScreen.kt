package com.respire.mvi.ui.screens.themeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.respire.mvi.R
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.screens.settingsScreen.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onBackPressed: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_theme)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeOption(
                title = stringResource(R.string.light),
                selected = currentTheme == ThemeMode.LIGHT,
                onClick = {
                    onThemeSelected(ThemeMode.LIGHT)
                    viewModel.saveTheme(ThemeMode.LIGHT)
                }
            )

            HorizontalDivider()

            ThemeOption(
                title = stringResource(R.string.dark),
                selected = currentTheme == ThemeMode.DARK,
                onClick = {
                    onThemeSelected(ThemeMode.DARK)
                    viewModel.saveTheme(ThemeMode.DARK)
                }
            )

            HorizontalDivider()

            ThemeOption(
                title = stringResource(R.string.system),
                selected = currentTheme == ThemeMode.SYSTEM,
                onClick = {
                    onThemeSelected(ThemeMode.SYSTEM)
                    viewModel.saveTheme(ThemeMode.SYSTEM)
                }
            )

            HorizontalDivider()
        }
    }
}

@Composable
private fun ThemeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
} 