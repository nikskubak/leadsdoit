package com.androsuperbooster.horoscope_feature.data.repo

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import com.androsuperbooster.horoscope_feature.R
import com.androsuperbooster.horoscope_feature.data.HawkKeys
import com.androsuperbooster.horoscope_feature.data.HawkKeys.FIRST_LAUNCH_WEEK
import com.androsuperbooster.horoscope_feature.data.InstallReferrerConstants.UTM_SOURCE
import com.androsuperbooster.horoscope_feature.data.ZodiacSignIds
import com.androsuperbooster.horoscope_feature.data.sources.network.models.response.RemoteConfigActionResponse
import com.androsuperbooster.horoscope_feature.data.sources.network.models.response.RemoteConfigZodiacsResponse
import com.androsuperbooster.horoscope_feature.domain.model.Action
import com.androsuperbooster.horoscope_feature.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope_feature.domain.repo.ZodiacRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ZodiacRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ZodiacRepository {
    companion object {
        const val DAILY_HOROSCOPE = "DAILY_HOROSCOPE"
        const val HOROSCOPE_DETAILS = "DETAILS"
        const val TURKISH = "tr"
        const val ENGLISH = "en"
    }

    val gson = Gson()

    override fun getZodiacSigns(): Flow<Result<List<ZodiacEntity>>> {
        return flow {
            try {
                remoteConfig.fetchAndActivate().await()
                val list = zodiacEntityList()
                emit(Result.success(list))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    private fun zodiacEntityList(): List<ZodiacEntity> {
        val locale = Locale.getDefault()
        val type = object : TypeToken<RemoteConfigZodiacsResponse?>() {}.type
        val response: RemoteConfigZodiacsResponse? = gson.fromJson<RemoteConfigZodiacsResponse?>(
            remoteConfig.getString(DAILY_HOROSCOPE),
            type
        )
        Log.e("zodiacAction", locale.language)
        val list = when (locale.language) {
            TURKISH -> {
                response?.tr
            }

            else -> {
                response?.en
            }
        }.orEmpty()
        val action = zodiacAction()
        Log.e("zodiacAction", action.toString())
        list.onEachIndexed { index, it ->
            setAdditionalData(
                it,
                getDayliHoroscope(index, list),
                action
            )
        }
        return list
    }

    private fun zodiacAction(): Action? {
        val type = object : TypeToken<RemoteConfigActionResponse?>() {}.type
        val string = remoteConfig.getString(HOROSCOPE_DETAILS)
        Log.e("string", string)
        val actionResponse = gson.fromJson<RemoteConfigActionResponse?>(
            string,
            type
        )
        actionResponse?.let {
            var action = when (Locale.getDefault().language) {
                TURKISH -> {
                    actionResponse.action?.tr
                }

                else -> {
                    actionResponse.action?.en
                }
            }
            val params = Hawk.get(HawkKeys.INSTALL_REFERRER, "")
            actionResponse.details?.let {
                if (params.isNotEmpty()) {
                    actionResponse.details += "?$params"
                }
            }

            if (actionResponse.id.isNullOrEmpty()) {
                return null
            } else {
                if (actionResponse.accessOnlyFor.isNullOrEmpty() || actionResponse.accessOnlyFor.contains(
                        parseReferrer(params)[UTM_SOURCE]
                    )
                ) {
                    return Action(actionResponse.id, actionResponse.details, action)
                } else {
                    return null
                }
            }
        } ?: run {
            return null
        }
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

    fun parseReferrer(referrer: String): Map<String, String> {
        return referrer.split("&").mapNotNull {
            val pair = it.split("=")
            if (pair.size == 2) pair[0] to pair[1] else null
        }.toMap()
    }
}