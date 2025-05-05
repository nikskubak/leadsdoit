package com.androsuperbooster.horoscope.domain.useCases

import com.androsuperbooster.horoscope.domain.repo.InstallReferrerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InstallReferrerUseCase @Inject constructor(
    private val installReferrerRepository: InstallReferrerRepository
) {
    operator fun invoke(): Flow<Map<String, String>?> {
        return installReferrerRepository.getInstallReferrerParams()
    }
}