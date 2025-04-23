package com.respire.baseapp.domain.repo

import com.respire.baseapp.domain.model.SettingsEntity
import com.respire.baseapp.domain.model.ZodiacEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Result<SettingsEntity>>
    fun saveSettings(settingsEntity: SettingsEntity): Flow<Result<Boolean>>
}