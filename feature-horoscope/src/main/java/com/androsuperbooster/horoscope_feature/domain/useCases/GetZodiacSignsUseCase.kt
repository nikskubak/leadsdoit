package com.androsuperbooster.horoscope_feature.domain.useCases

import com.androsuperbooster.horoscope_feature.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope_feature.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetZodiacSignsUseCase @Inject constructor(
    private val repository: ZodiacRepository
) {
    operator fun invoke(): Flow<Result<List<ZodiacEntity>>> {
        return repository.getZodiacSigns()
    }
}