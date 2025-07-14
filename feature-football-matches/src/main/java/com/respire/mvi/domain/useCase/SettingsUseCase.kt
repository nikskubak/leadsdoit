package com.respire.mvi.domain.useCase

import com.respire.mvi.domain.model.SettingsEntity
import com.respire.mvi.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun getSettings() :  Flow<Result<SettingsEntity>> {
        return settingsRepository.getSettings()
    }

    fun saveSettings(settingsEntity: SettingsEntity?) :  Flow<Result<Boolean>> {
        return settingsRepository.saveSettings(settingsEntity)
    }
}