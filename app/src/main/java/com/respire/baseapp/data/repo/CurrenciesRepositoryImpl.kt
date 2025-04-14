package com.respire.baseapp.data.repo

import com.respire.baseapp.data.mapToCurrencyDb
import com.respire.baseapp.data.mapToCurrencyEntity
import com.respire.baseapp.data.sources.database.dao.CurrencyDao
import com.respire.baseapp.data.sources.network.CurrenciesApi
import com.respire.baseapp.domain.model.CurrencyEntity
import com.respire.baseapp.domain.repo.CurrenciesRepository
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