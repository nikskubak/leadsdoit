package com.androsuperbooster.horoscope_feature.domain.model

import com.androsuperbooster.horoscope_feature.ui.themeScreen.ThemeMode

data class SettingsEntity(
    val themeMode: ThemeMode? = null,
    val isNotificationEnabled: Boolean? = null
)
