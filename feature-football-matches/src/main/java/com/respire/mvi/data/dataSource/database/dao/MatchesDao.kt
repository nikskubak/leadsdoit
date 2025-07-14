package com.respire.mvi.data.dataSource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.respire.mvi.data.dataSource.database.models.MatchesDB


@Dao
interface MatchesDao {

    @Query("SELECT * FROM matches")
    suspend fun getMatches(): List<MatchesDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchesDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(matchesDB: MatchesDB)

    @Update
    suspend fun updateMatch(matchesDB: MatchesDB)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun removeMatch(id: Long)
}