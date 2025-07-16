package com.respire.mvi.data.dataSource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.respire.mvi.data.dataSource.database.models.FixtureDB

@Dao
interface MatchesDao {
    @Query("SELECT * FROM fixtures WHERE fixture_date = :date")
    suspend fun getFixturesByDate(date: String): List<FixtureDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FixtureDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixture(fixture: FixtureDB)

    @Query("DELETE FROM fixtures WHERE id = :id")
    suspend fun removeFixture(id: Int)
}