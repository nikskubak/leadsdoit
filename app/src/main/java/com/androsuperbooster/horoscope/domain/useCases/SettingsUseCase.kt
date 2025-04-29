package com.androsuperbooster.horoscope.domain.useCases

import com.androsuperbooster.horoscope.domain.model.SettingsEntity
import com.androsuperbooster.horoscope.domain.repo.SettingsRepository
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