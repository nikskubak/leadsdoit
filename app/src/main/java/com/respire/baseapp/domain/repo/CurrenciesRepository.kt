package com.respire.baseapp.domain.repo

import com.respire.baseapp.domain.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {
    fun getCurrencies(): Flow<Result<List<CurrencyEntity>>>
}