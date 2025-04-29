package com.androsuperbooster.horoscope.domain.model

import com.androsuperbooster.horoscope.ui.themeScreen.ThemeMode

data class SettingsEntity(
    val themeMode: ThemeMode? = null,
    val isNotificationEnabled: Boolean? = null
)
