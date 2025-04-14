package com.respire.baseapp.domain.useCases

import android.content.Context
import com.respire.baseapp.domain.repo.CurrenciesRepository
import com.respire.baseapp.domain.model.CurrencyEntity
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