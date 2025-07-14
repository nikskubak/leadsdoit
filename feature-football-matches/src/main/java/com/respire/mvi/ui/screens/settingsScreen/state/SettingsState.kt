package com.respire.mvi.ui.screens.settingsScreen.state

import com.respire.mvi.domain.model.SettingsEntity
import com.respire.mvi.ui.base.ui.theme.ThemeMode

data class SettingsItem(
    var icon: Int,
    var title: String,
    var description: String,
    var witSwitcher : Boolean,
    var switcherValue : Boolean
)

enum class SettingsAction{
    THEMES, PRIVACY_POLICY, NOTIFICATIONS
}

data class SettingsState(
    val settingsEntity: SettingsEntity? = null
)


sealed class SettingsEvent {
    object LoadSettings : SettingsEvent()
    data class OnThemeChanged(val themeMode: ThemeMode) : SettingsEvent()
    object OnPrivacyClicked : SettingsEvent()
    data class OnNotificationChanged(val isEnable: Boolean) : SettingsEvent()
}

sealed class SettingsEffect {
    object NavigateToThemes : SettingsEffect()
    data class NavigateToPrivacyPolicy(val url: String? = null) : SettingsEffect()
}