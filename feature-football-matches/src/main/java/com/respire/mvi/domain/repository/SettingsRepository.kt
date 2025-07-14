package com.respire.mvi.domain.repository

import com.respire.mvi.domain.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Result<SettingsEntity>>
    fun saveSettings(settingsEntity: SettingsEntity?): Flow<Result<Boolean>>
}