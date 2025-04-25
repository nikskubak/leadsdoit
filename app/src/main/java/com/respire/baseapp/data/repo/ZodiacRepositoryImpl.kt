package com.respire.baseapp.data.repo

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import com.respire.baseapp.R
import com.respire.baseapp.data.HawkKeys
import com.respire.baseapp.data.HawkKeys.FIRST_LAUNCH_WEEK
import com.respire.baseapp.data.ZodiacSignIds
import com.respire.baseapp.domain.model.Action
import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import javax.inject.Inject

class ZodiacRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ZodiacRepository {
    companion object {
        const val DAILY_HOROSCOPE = "DAILY_HOROSCOPE"
        const val HOROSCOPE_DETAILS = "DETAILS"
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

    private fun zodiacEntityList(): List<ZodiacEntity> {
        val type = object : TypeToken<List<ZodiacEntity>>() {}.type
        val list = gson.fromJson<List<ZodiacEntity>>(
            remoteConfig.getString(DAILY_HOROSCOPE),
            type
        ).orEmpty()
        val action = zodiacAction()
        Log.e("zodiacAction", action.toString())
        list.onEachIndexed { index, it -> setAdditionalData(it, getDayliHoroscope(index, list), action) }
        return list
    }

    private fun zodiacAction() : Action?{
        val type = object : TypeToken<Action?>() {}.type
        return gson.fromJson<Action?>(
            remoteConfig.getString(HOROSCOPE_DETAILS),
            type
        )
    }

    private fun getDayliHoroscope(index: Int, list: List<ZodiacEntity>): List<String>? {
        val newIndex = index + getDifferenceBetweenFirstLaunchWeek()
        return if (newIndex <= list.size - 1) {
            list[newIndex].dailyHoroscope
        } else {
            list[newIndex % list.size].dailyHoroscope
        }
    }

    override fun getSelectedZodiac(): Flow<Result<ZodiacEntity?>> {
        val zodiacEntity: ZodiacEntity? = Hawk.get<ZodiacEntity?>(HawkKeys.SELECTED_ZODIAC, null)
        return flowOf(Result.success(zodiacEntityList().find { it.id == zodiacEntity?.id }))
    }

    override fun saveSelectedZodiac(zodiacEntity: ZodiacEntity): Flow<Result<Boolean>> {
        Hawk.put(HawkKeys.SELECTED_ZODIAC, zodiacEntity)
        return flowOf(Result.success(true))
    }

    private fun setAdditionalData(
        zodiacEntity: ZodiacEntity,
        dailyHoroscope: List<String>?,
        action: Action?
    ) {
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
        zodiacEntity.apply {
            dailyHoroscope?.let {
                todayHoroscope = dailyHoroscope[getCurrentDayPosition() - 1]
            }
            action?.let {
                this.action = action
            }
        }
    }

    fun getCurrentDayPosition(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK).let { dayOfWeek ->
            // Calendar.DAY_OF_WEEK starts with Sunday as 1, we want Monday as 1
            if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1
        }
    }

    fun getDifferenceBetweenFirstLaunchWeek(): Int {
        val currentWeekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + 12
        val firstLaunchWeekOfYear: Int = Hawk.get(FIRST_LAUNCH_WEEK, currentWeekOfYear)
        if (firstLaunchWeekOfYear == currentWeekOfYear) {
            Hawk.put(FIRST_LAUNCH_WEEK, currentWeekOfYear)
        }
        return Math.abs(currentWeekOfYear - firstLaunchWeekOfYear)
    }
}