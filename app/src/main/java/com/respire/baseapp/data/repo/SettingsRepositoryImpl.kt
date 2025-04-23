package com.respire.baseapp.data.repo

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import com.respire.baseapp.R
import com.respire.baseapp.data.HawkKeys
import com.respire.baseapp.data.ZodiacSignIds
import com.respire.baseapp.domain.model.SettingsEntity
import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.domain.repo.SettingsRepository
import com.respire.baseapp.domain.repo.ZodiacRepository
import com.respire.baseapp.ui.themeScreen.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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