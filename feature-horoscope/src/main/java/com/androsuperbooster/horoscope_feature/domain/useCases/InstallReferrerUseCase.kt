package com.androsuperbooster.horoscope_feature.domain.useCases

import com.androsuperbooster.horoscope_feature.domain.repo.InstallReferrerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InstallReferrerUseCase @Inject constructor(
    private val installReferrerRepository: InstallReferrerRepository
) {
    operator fun invoke(): Flow<Map<String, String>?> {
        return installReferrerRepository.getInstallReferrerParams()
    }
}