package com.androsuperbooster.horoscope_feature.data.sources.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androsuperbooster.horoscope_feature.data.sources.database.models.CurrencyDB


@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    suspend fun getCurrencies(): List<CurrencyDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currencyDB: CurrencyDB)

    @Update
    suspend fun updateCurrency(currencyDB: CurrencyDB)

    @Query("DELETE FROM currency WHERE id = :id")
    suspend fun removeCurrency(id: Long)
}