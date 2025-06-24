package com.androsuperbooster.horoscope_feature.domain.useCases

import com.androsuperbooster.horoscope_feature.domain.model.SettingsEntity
import com.androsuperbooster.horoscope_feature.domain.repo.SettingsRepository
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