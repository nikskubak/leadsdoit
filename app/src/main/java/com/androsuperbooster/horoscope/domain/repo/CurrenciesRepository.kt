package com.androsuperbooster.horoscope.domain.repo

import com.androsuperbooster.horoscope.domain.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {
    fun getCurrencies(): Flow<Result<List<CurrencyEntity>>>
}