package com.androsuperbooster.horoscope_feature.domain.repo

import com.androsuperbooster.horoscope_feature.domain.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {
    fun getCurrencies(): Flow<Result<List<CurrencyEntity>>>
}