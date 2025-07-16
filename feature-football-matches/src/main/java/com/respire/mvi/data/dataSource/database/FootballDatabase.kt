package com.respire.mvi.data.dataSource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.respire.mvi.data.dataSource.database.dao.MatchesDao
import com.respire.mvi.data.dataSource.database.models.FixtureDB

@Database(
    entities = [FixtureDB::class],
    version = 1,
    exportSchema = true
)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun getMatchesDao(): MatchesDao
}