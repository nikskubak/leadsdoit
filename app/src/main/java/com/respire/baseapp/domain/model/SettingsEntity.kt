package com.respire.baseapp.domain.model

import com.respire.baseapp.ui.themeScreen.ThemeMode

data class SettingsEntity(
    val themeMode: ThemeMode? = null,
    val isNotificationEnabled: Boolean? = null
)
