package com.respire.mvi.data.dataSource.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchesDB(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "rank") var rank: Int? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "symbol") var symbol: String? = null,
    @ColumnInfo(name = "slug") var slug: String? = null,
    @ColumnInfo(name = "is_active") var isActive: Int? = null,
    @ColumnInfo(name = "first_historical_data") var firstHistoricalData: String? = null,
    @ColumnInfo(name = "last_historical_data") var lastHistoricalData: String? = null,
    @ColumnInfo(name = "platform") var platform: String? = null
)