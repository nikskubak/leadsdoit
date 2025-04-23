package com.respire.baseapp.data

import com.respire.baseapp.data.sources.database.models.CurrencyDB
import com.respire.baseapp.data.sources.network.models.response.BaseResponse
import com.respire.baseapp.data.sources.network.models.response.CurrencyResponse
import com.respire.baseapp.domain.model.CurrencyEntity

fun mapToCurrencyEntity(currencyResponse: CurrencyResponse): CurrencyEntity {
    return CurrencyEntity(
        currencyResponse.id,
        currencyResponse.rank,
        currencyResponse.name,
        currencyResponse.symbol,
        currencyResponse.slug,
        currencyResponse.isActive,
        currencyResponse.firstHistoricalData,
        currencyResponse.lastHistoricalData,
        currencyResponse.platform
    )
}

fun mapToCurrencyEntity(currencyDB: CurrencyDB): CurrencyEntity {
    return CurrencyEntity(
        currencyDB.id,
        currencyDB.rank,
        currencyDB.name,
        currencyDB.symbol,
        currencyDB.slug,
        currencyDB.isActive,
        currencyDB.firstHistoricalData,
        currencyDB.lastHistoricalData,
        currencyDB.platform
    )
}

fun mapToCurrencyDb(currencyResponse: CurrencyResponse): CurrencyDB {
    return CurrencyDB(
        currencyResponse.id,
        currencyResponse.rank,
        currencyResponse.name,
        currencyResponse.symbol,
        currencyResponse.slug,
        currencyResponse.isActive,
        currencyResponse.firstHistoricalData,
        currencyResponse.lastHistoricalData,
        currencyResponse.platform
    )
}

object ZodiacMapper{

}