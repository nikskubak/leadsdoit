package com.androsuperbooster.horoscope.domain.useCases

import com.androsuperbooster.horoscope.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetZodiacSignsUseCase @Inject constructor(
    private val repository: ZodiacRepository
) {
    operator fun invoke(): Flow<Result<List<ZodiacEntity>>> {
        return repository.getZodiacSigns()
    }
}