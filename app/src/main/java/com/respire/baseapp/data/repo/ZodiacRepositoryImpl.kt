package com.respire.baseapp.data.repo

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import com.respire.baseapp.R
import com.respire.baseapp.data.HawkKeys
import com.respire.baseapp.data.ZodiacSignIds
import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ZodiacRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ZodiacRepository {
    companion object {
        const val DAILY_HOROSCOPE = "DAILY_HOROSCOPE"
    }

    val gson = Gson()

    override fun getZodiacSigns(): Flow<Result<List<ZodiacEntity>>> {
        return flow {
            try {
                val list = zodiacEntityList()
                emit(Result.success(list))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    private fun zodiacEntityList(): List<ZodiacEntity>{
        val type = object : TypeToken<List<ZodiacEntity>>() {}.type
        val list = gson.fromJson<List<ZodiacEntity>>(
            remoteConfig.getString(DAILY_HOROSCOPE),
            type
        ).orEmpty().onEach { setAnimations(it) }
        return list
    }

    override fun getSelectedZodiac(): Flow<Result<ZodiacEntity?>> {
        val zodiacEntity: ZodiacEntity? = Hawk.get<ZodiacEntity?>(HawkKeys.SELECTED_ZODIAC, null)
        return flowOf(Result.success(zodiacEntityList().find { it.id == zodiacEntity?.id }))
    }

    override fun saveSelectedZodiac(zodiacEntity: ZodiacEntity): Flow<Result<Boolean>> {
        Hawk.put(HawkKeys.SELECTED_ZODIAC, zodiacEntity)
        return flowOf(Result.success(true))
    }

    private fun setAnimations(zodiacEntity: ZodiacEntity) {
        when (zodiacEntity.id?.toInt()) {
            ZodiacSignIds.ARIES -> zodiacEntity.animationRes = R.drawable.ic_oven
            ZodiacSignIds.TAURUS -> zodiacEntity.animationRes = R.drawable.ic_taurus
            ZodiacSignIds.GEMINI -> zodiacEntity.animationRes = R.drawable.ic_gemini
            ZodiacSignIds.CANCER -> zodiacEntity.animationRes = R.drawable.ic_cancer
            ZodiacSignIds.LEO -> zodiacEntity.animationRes = R.drawable.ic_lion
            ZodiacSignIds.VIRGO -> zodiacEntity.animationRes = R.drawable.ic_virgo
            ZodiacSignIds.LIBRA -> zodiacEntity.animationRes = R.drawable.ic_libra
            ZodiacSignIds.SCORPIO -> zodiacEntity.animationRes = R.drawable.ic_scorpion
            ZodiacSignIds.SAGITTARIUS -> zodiacEntity.animationRes = R.drawable.ic_sagitar
            ZodiacSignIds.CAPRICORN -> zodiacEntity.animationRes = R.drawable.ic_x
            ZodiacSignIds.AQUARIUS -> zodiacEntity.animationRes = R.drawable.ic_aqua
            ZodiacSignIds.PISCES -> zodiacEntity.animationRes = R.drawable.ic_fishes
        }
    }
}