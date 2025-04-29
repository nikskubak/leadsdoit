package com.androsuperbooster.horoscope.domain.repo

import com.androsuperbooster.horoscope.domain.model.ZodiacEntity
import kotlinx.coroutines.flow.Flow

interface ZodiacRepository {
    fun getZodiacSigns(): Flow<Result<List<ZodiacEntity>>>
    fun getSelectedZodiac(): Flow<Result<ZodiacEntity?>>
    fun saveSelectedZodiac(zodiacEntity: ZodiacEntity): Flow<Result<Boolean>>
}