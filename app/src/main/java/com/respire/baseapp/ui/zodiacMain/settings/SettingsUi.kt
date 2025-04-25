package com.respire.baseapp.ui.zodiacMain.settings

import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.ui.themeScreen.ThemeMode

data class SettingsUi(var zodiacEntity: ZodiacEntity,
                      var themeMode: ThemeMode,
                      var isNotificationEnabled : Boolean)