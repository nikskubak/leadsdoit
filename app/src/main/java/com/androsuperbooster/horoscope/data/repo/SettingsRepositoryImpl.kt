package com.androsuperbooster.horoscope.data.repo

import com.orhanobut.hawk.Hawk
import com.androsuperbooster.horoscope.data.HawkKeys
import com.androsuperbooster.horoscope.domain.model.SettingsEntity
import com.androsuperbooster.horoscope.domain.repo.SettingsRepository
import com.androsuperbooster.horoscope.ui.themeScreen.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

    override fun getSettings(): Flow<Result<SettingsEntity>> {
        val themeMode = Hawk.get(HawkKeys.THEME, ThemeMode.SYSTEM)
        val isNotificationEnabled = Hawk.get(HawkKeys.IS_NOTIFICATION_ENABLED, true)
        return flowOf(Result.success(SettingsEntity(themeMode, isNotificationEnabled)))
    }

    override fun saveSettings(settingsEntity: SettingsEntity): Flow<Result<Boolean>> {
        settingsEntity.themeMode?.let {
            Hawk.put(HawkKeys.THEME, settingsEntity.themeMode)
        }
        settingsEntity.isNotificationEnabled?.let {
            Hawk.put(HawkKeys.IS_NOTIFICATION_ENABLED, settingsEntity.isNotificationEnabled)
        }
        return flowOf(Result.success(true))
    }
}