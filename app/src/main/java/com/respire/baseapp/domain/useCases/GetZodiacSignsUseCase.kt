package com.respire.baseapp.domain.useCases

import android.content.Context
import com.respire.baseapp.domain.repo.CurrenciesRepository
import com.respire.baseapp.domain.model.CurrencyEntity
import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetZodiacSignsUseCase @Inject constructor(
    private val repository: ZodiacRepository
) {
    operator fun invoke(): Flow<Result<List<ZodiacEntity>>> {
        return repository.getZodiacSigns()
    }
}