package com.androsuperbooster.horoscope_feature.domain.useCases

import android.content.Context
import com.androsuperbooster.horoscope_feature.domain.repo.CurrenciesRepository
import com.androsuperbooster.horoscope_feature.domain.model.CurrencyEntity
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