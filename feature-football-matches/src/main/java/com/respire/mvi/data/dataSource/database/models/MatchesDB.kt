package com.respire.mvi.data.dataSource.database.models

import androidx.room.*

@Entity(tableName = "fixtures")
data class FixtureDB(
    @PrimaryKey val id: Int,
    @Embedded(prefix = "fixture_") val fixture: FixtureDetailDB,
    @Embedded(prefix = "league_") val league: LeagueDB,
    @Embedded(prefix = "goals_") val goals: GoalsDB,
    @Embedded(prefix = "score_") val score: ScoreDB,
    @Embedded(prefix = "home_") val homeTeam: TeamDB,
    @Embedded(prefix = "away_") val awayTeam: TeamDB
)

// Embedded/related DB models

data class FixtureDetailDB(
    val referee: String?,
    val timezone: String,
    val date: String,
    val time: String,
    val timestamp: Long,
    @Embedded(prefix = "periods_") val periods: PeriodsDB,
    @Embedded(prefix = "venue_") val venue: VenueDB?,
    @Embedded(prefix = "status_") val status: StatusDB
)

data class PeriodsDB(
    val first: Long?,
    val second: Long?
)

data class VenueDB(
    val id: Int?,
    val name: String?,
    val city: String?
)

data class LeagueDB(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String?,
    val flag: String?,
    val season: Int,
    val round: String
)

data class TeamDB(
    val id: Int,
    val name: String,
    val logo: String?,
    val winner: Boolean?
)

data class GoalsDB(
    val home: Int?,
    val away: Int?
)

data class ScoreDB(
    @Embedded(prefix = "halftime_") val halftime: GoalsDB?,
    @Embedded(prefix = "fulltime_") val fulltime: GoalsDB?,
    @Embedded(prefix = "extratime_") val extratime: GoalsDB?,
    @Embedded(prefix = "penalty_") val penalty: GoalsDB?
)

data class StatusDB(
    val long: String,
    val short: String,
    val elapsed: Int?,
    val isLive: Boolean,
    val hasStarted: Boolean
)