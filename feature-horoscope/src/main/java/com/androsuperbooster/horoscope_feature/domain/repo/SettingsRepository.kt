package com.androsuperbooster.horoscope_feature.domain.repo

import com.androsuperbooster.horoscope_feature.domain.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Result<SettingsEntity>>
    fun saveSettings(settingsEntity: SettingsEntity): Flow<Result<Boolean>>
}