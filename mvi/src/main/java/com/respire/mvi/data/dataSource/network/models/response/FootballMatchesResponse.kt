package com.respire.mvi.data.dataSource.network.models.response

import com.google.gson.annotations.SerializedName

// Base API Response wrapper
data class FootballMatchesResponse<T>(
    val get: String,
    val parameters: Map<String, Any>?,
    val errors: List<String>?,
    val results: Int,
    val paging: Paging?,
    val response: List<T>
)

data class Paging(
    val current: Int,
    val total: Int
)

// Fixtures Response Models
data class FixtureResponse(
    val fixture: Fixture,
    val league: League,
    val teams: Teams,
    val goals: Goals,
    val score: Score
)

data class Fixture(
    val id: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periods: Periods,
    val venue: Venue?,
    val status: Status
)

data class Periods(
    val first: Long?,
    val second: Long?
)

data class Venue(
    val id: Int?,
    val name: String?,
    val city: String?
)

data class Status(
    val long: String,
    val short: String,
    val elapsed: Int?
)

data class League(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val round: String,
    val type: String
)

data class Teams(
    val home: Team,
    val away: Team
)

data class Team(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean?
)

data class Goals(
    val home: Int?,
    val away: Int?
)

data class Score(
    val halftime: Goals,
    val fulltime: Goals,
    val extratime: Goals?,
    val penalty: Goals?
)

// Extended Fixture Models for Detailed Information
data class DetailedFixtureResponse(
    val fixture: DetailedFixture,
    val league: DetailedLeague,
    val teams: DetailedTeams,
    val goals: DetailedGoals,
    val score: DetailedScore,
    val events: List<Event>?,
    val lineups: Lineups?,
    val statistics: List<Statistics>?,
    val players: Players?
)

data class DetailedFixture(
    val id: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periods: Periods,
    val venue: Venue?,
    val status: Status
)

data class DetailedLeague(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val round: String,
    val type: String
)

data class DetailedTeams(
    val home: DetailedTeam,
    val away: DetailedTeam
)

data class DetailedTeam(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean?,
    val colors: TeamColors?
)

data class TeamColors(
    val player: TeamColor?,
    val goalkeeper: TeamColor?
)

data class TeamColor(
    val primary: String?,
    val number: String?,
    val border: String?
)

data class DetailedGoals(
    val home: Int?,
    val away: Int?
)

data class DetailedScore(
    val halftime: DetailedGoals,
    val fulltime: DetailedGoals,
    val extratime: DetailedGoals?,
    val penalty: DetailedGoals?
)

// Event Models
data class Event(
    val time: EventTime,
    val team: EventTeam,
    val player: EventPlayer,
    val assist: EventPlayer?,
    val type: String,
    val detail: String,
    val comments: String?
)

data class EventTime(
    val elapsed: Int?,
    val extra: Int?
)

data class EventTeam(
    val id: Int,
    val name: String,
    val logo: String,
    val colors: TeamColors?
)

data class EventPlayer(
    val id: Int,
    val name: String
)

// Lineups Models
data class Lineups(
    val home: Lineup?,
    val away: Lineup?
)

data class Lineup(
    val team: LineupTeam,
    val formation: String,
    val startXI: List<LineupPlayer>,
    val substitutes: List<LineupPlayer>,
    val coach: Coach,
    val colors: TeamColors?
)

data class LineupTeam(
    val id: Int,
    val name: String,
    val logo: String,
    val colors: TeamColors?
)

data class LineupPlayer(
    val player: Player
)

data class Player(
    val id: Int,
    val name: String,
    val number: Int,
    val pos: String,
    val grid: String?
)

data class Coach(
    val id: Int,
    val name: String,
    val photo: String?
)

// Statistics Models
data class Statistics(
    val team: StatisticsTeam,
    val statistics: List<Statistic>
)

data class StatisticsTeam(
    val id: Int,
    val name: String,
    val logo: String
)

data class Statistic(
    val type: String,
    val value: Any?
)

// Players Models
data class Players(
    val home: List<PlayerData>?,
    val away: List<PlayerData>?
)

data class PlayerData(
    val team: PlayerTeam,
    val players: List<PlayerInfo>
)

data class PlayerTeam(
    val id: Int,
    val name: String,
    val logo: String,
    val update: String
)

data class PlayerInfo(
    val player: PlayerDetails,
    val statistics: List<PlayerStatistic>
)

data class PlayerDetails(
    val id: Int,
    val name: String,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val nationality: String,
    val height: String?,
    val weight: String?,
    val injured: Boolean,
    val photo: String
)

data class PlayerStatistic(
    val team: PlayerTeam,
    val league: PlayerLeague,
    val games: PlayerGames,
    val substitutes: PlayerSubstitutes,
    val shots: PlayerShots,
    val goals: PlayerGoals,
    val passes: PlayerPasses,
    val tackles: PlayerTackles,
    val duels: PlayerDuels,
    val dribbles: PlayerDribbles,
    val fouls: PlayerFouls,
    val cards: PlayerCards,
    val penalty: PlayerPenalty
)

data class PlayerLeague(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int
)

data class PlayerGames(
    val appearances: Int?,
    val lineups: Int?,
    val minutes: Int?,
    val number: Int?,
    val position: String?,
    val rating: String?,
    val captain: Boolean?
)

data class PlayerSubstitutes(
    @SerializedName("in")
    val inValue: Int?,
    val out: Int?,
    val bench: Int?
)

data class PlayerShots(
    val total: Int?,
    val on: Int?
)

data class PlayerGoals(
    val total: Int?,
    val conceded: Int?,
    val assists: Int?,
    val saves: Int?
)

data class PlayerPasses(
    val total: Int?,
    val key: Int?,
    val accuracy: String?
)

data class PlayerTackles(
    val total: Int?,
    val blocks: Int?,
    val interceptions: Int?
)

data class PlayerDuels(
    val total: Int?,
    val won: Int?
)

data class PlayerDribbles(
    val attempts: Int?,
    val success: Int?,
    val past: Int?
)

data class PlayerFouls(
    val drawn: Int?,
    val committed: Int?
)

data class PlayerCards(
    val yellow: Int?,
    val red: Int?
)

data class PlayerPenalty(
    val won: Int?,
    val committed: Int?,
    val scored: Int?,
    val missed: Int?,
    val saved: Int?
)

