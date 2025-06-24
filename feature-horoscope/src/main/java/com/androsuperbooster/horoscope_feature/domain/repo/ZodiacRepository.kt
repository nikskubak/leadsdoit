package com.androsuperbooster.horoscope_feature.domain.repo

import com.androsuperbooster.horoscope_feature.domain.model.ZodiacEntity
import kotlinx.coroutines.flow.Flow

interface ZodiacRepository {
    fun getZodiacSigns(): Flow<Result<List<ZodiacEntity>>>
    fun getSelectedZodiac(): Flow<Result<ZodiacEntity?>>
    fun saveSelectedZodiac(zodiacEntity: ZodiacEntity): Flow<Result<Boolean>>
}