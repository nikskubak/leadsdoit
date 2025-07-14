package com.respire.mvi.data.repository

import com.orhanobut.hawk.Hawk
import com.respire.mvi.data.Const
import com.respire.mvi.data.HawkKeys
import com.respire.mvi.domain.model.SettingsEntity
import com.respire.mvi.domain.repository.SettingsRepository
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

    override fun getSettings(): Flow<Result<SettingsEntity>> {
        val themeMode = Hawk.get(HawkKeys.THEME, ThemeMode.SYSTEM.ordinal)
        val isNotificationEnabled = Hawk.get(HawkKeys.IS_NOTIFICATION_ENABLED, true)
        val privacyPolicyUrl = Hawk.get(HawkKeys.PRIVACY_POLICY_URL, Const.PRIVACY_POLICY_URL)
        return flowOf(Result.success(SettingsEntity(themeMode, isNotificationEnabled, privacyPolicyUrl)))
    }

    override fun saveSettings(settingsEntity: SettingsEntity?): Flow<Result<Boolean>> {
        settingsEntity?.themeMode?.let {
            Hawk.put(HawkKeys.THEME, settingsEntity.themeMode)
        }
        settingsEntity?.isNotificationEnabled?.let {
            Hawk.put(HawkKeys.IS_NOTIFICATION_ENABLED, settingsEntity.isNotificationEnabled)
        }
        return flowOf(Result.success(true))
    }
}