package com.respire.baseapp.data.sources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.respire.baseapp.data.sources.database.dao.CurrencyDao
import com.respire.baseapp.data.sources.database.models.CurrencyDB

@Database(
    entities = [CurrencyDB::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
}