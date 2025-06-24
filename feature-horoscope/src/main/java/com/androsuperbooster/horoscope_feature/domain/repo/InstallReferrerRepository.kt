package com.androsuperbooster.horoscope_feature.domain.repo

import kotlinx.coroutines.flow.Flow

interface InstallReferrerRepository {
    fun getInstallReferrerParams(): Flow<Map<String, String>?>
}