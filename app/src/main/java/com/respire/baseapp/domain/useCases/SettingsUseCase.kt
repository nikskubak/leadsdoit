package com.respire.baseapp.domain.useCases

import com.respire.baseapp.domain.model.SettingsEntity
import com.respire.baseapp.domain.repo.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun getSettings() :  Flow<Result<SettingsEntity>> {
        return settingsRepository.getSettings()
    }

    fun saveSettings(settingsEntity: SettingsEntity) :  Flow<Result<Boolean>> {
        return settingsRepository.saveSettings(settingsEntity)
    }
}