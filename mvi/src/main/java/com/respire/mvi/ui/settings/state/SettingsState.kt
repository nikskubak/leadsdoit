package com.respire.mvi.ui.settings.state

data class SettingsItem(
    val title: String,
    val icon: Int,
    val action: SettingsAction
)

enum class SettingsAction{
    THEMES, PRIVACY_POLICY
}

data class SettingsState(
    val settingsItems: List<SettingsItem> = emptyList()
)

sealed class SettingsEvent {
    object LoadSettings : SettingsEvent()
    data class OnSettingsItemClicked(val action: SettingsAction) : SettingsEvent()
}

sealed class SettingsEffect {
    object NavigateToThemes : SettingsEffect()
    object NavigateToPrivacyPolicy : SettingsEffect()
}