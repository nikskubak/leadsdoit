package com.androsuperbooster.horoscope.ui.zodiacMain.settings

import com.androsuperbooster.horoscope.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope.ui.themeScreen.ThemeMode

data class SettingsUi(var zodiacEntity: ZodiacEntity,
                      var themeMode: ThemeMode,
                      var isNotificationEnabled : Boolean)