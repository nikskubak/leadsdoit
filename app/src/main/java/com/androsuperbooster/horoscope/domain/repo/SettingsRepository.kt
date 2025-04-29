package com.androsuperbooster.horoscope.domain.repo

import com.androsuperbooster.horoscope.domain.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Result<SettingsEntity>>
    fun saveSettings(settingsEntity: SettingsEntity): Flow<Result<Boolean>>
}