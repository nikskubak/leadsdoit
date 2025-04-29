package com.androsuperbooster.horoscope.domain.useCases

import com.androsuperbooster.horoscope.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SelectedZodiacUseCase @Inject constructor(
    private val repository: ZodiacRepository
) {

    fun getSelectedZodiac() :  Flow<Result<ZodiacEntity?>> {
        return repository.getSelectedZodiac()
    }

    fun saveSelectedZodiac(zodiacEntity: ZodiacEntity) :  Flow<Result<Boolean>> {
        return repository.saveSelectedZodiac(zodiacEntity)
    }
}