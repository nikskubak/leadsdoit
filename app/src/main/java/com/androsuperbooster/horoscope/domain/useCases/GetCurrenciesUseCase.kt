package com.androsuperbooster.horoscope.domain.useCases

import android.content.Context
import com.androsuperbooster.horoscope.domain.repo.CurrenciesRepository
import com.androsuperbooster.horoscope.domain.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    private val context: Context
) {
    operator fun invoke(): Flow<Result<List<CurrencyEntity>>> {
        return repository.getCurrencies()
    }
}