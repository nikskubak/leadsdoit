package com.androsuperbooster.horoscope_feature.data.sources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androsuperbooster.horoscope_feature.data.sources.database.dao.CurrencyDao
import com.androsuperbooster.horoscope_feature.data.sources.database.models.CurrencyDB

@Database(
    entities = [CurrencyDB::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
}