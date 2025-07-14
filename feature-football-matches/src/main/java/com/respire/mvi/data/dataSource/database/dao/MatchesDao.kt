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
    suspend fun getCurrencies(): List<MatchesDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<MatchesDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(matchesDB: MatchesDB)

    @Update
    suspend fun updateCurrency(matchesDB: MatchesDB)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun removeCurrency(id: Long)
}