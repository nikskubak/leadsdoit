package com.androsuperbooster.horoscope.domain.repo

import kotlinx.coroutines.flow.Flow

interface InstallReferrerRepository {
    fun getInstallReferrerParams(): Flow<Map<String, String>?>
}