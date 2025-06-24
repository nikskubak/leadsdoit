package com.androsuperbooster.horoscope_feature.ui.zodiacMain.settings

import com.androsuperbooster.horoscope_feature.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope_feature.ui.themeScreen.ThemeMode

data class SettingsUi(var zodiacEntity: ZodiacEntity,
                      var themeMode: ThemeMode,
                      var isNotificationEnabled : Boolean)