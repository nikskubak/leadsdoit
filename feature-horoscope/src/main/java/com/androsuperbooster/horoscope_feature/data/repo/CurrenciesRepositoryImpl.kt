package com.androsuperbooster.horoscope_feature.data.repo

import com.androsuperbooster.horoscope_feature.data.mapToCurrencyDb
import com.androsuperbooster.horoscope_feature.data.mapToCurrencyEntity
import com.androsuperbooster.horoscope_feature.data.sources.database.dao.CurrencyDao
import com.androsuperbooster.horoscope_feature.data.sources.network.CurrenciesApi
import com.androsuperbooster.horoscope_feature.domain.model.CurrencyEntity
import com.androsuperbooster.horoscope_feature.domain.repo.CurrenciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val currenciesApi: CurrenciesApi
) : CurrenciesRepository {
    override fun getCurrencies(): Flow<Result<List<CurrencyEntity>>> {
        return flow {
            try {
                val networkData = currenciesApi.getCurrencies(getHeaders())
                if (networkData.isSuccessful) {
                    currencyDao.insertCurrencies(networkData.body()?.data?.map { mapToCurrencyDb(it) }
                        ?: emptyList())
                    emit(Result.success(networkData.body()?.data?.map { mapToCurrencyEntity(it) }
                        ?: emptyList()))
                } else {
                    emit(
                        Result.success(getCurrenciesFromDb())
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Result.success(getCurrenciesFromDb())
                )
            }
        }
    }

    private fun getHeaders(): Map<String, String> {
        return mapOf()
    }

    suspend fun getCurrenciesFromDb() : List<CurrencyEntity>{
        return currencyDao.getCurrencies().map { mapToCurrencyEntity(it) }
    }
}