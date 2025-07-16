package com.respire.mvi.domain.model

data class FootballMatchesEntities<T>(
    val get: String,
    val parameters: Map<String, Any>?,
    val errors: List<String>?,
    val results: Int,
    val paging: PagingEntity?,
    val response: List<T>
)

data class PagingEntity(
    val current: Int,
    val total: Int
)

data class FixtureEntity(
    val fixture: FixtureDetailEntity,
    val league: LeagueEntity,
    val teams: TeamsEntity,
    val goals: GoalsEntity,
    val score: ScoreEntity
)

data class FixtureDetailEntity(
    val id: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val time: String,
    val timestamp: Long,
    val periods: PeriodsEntity,
    val venue: VenueEntity?,
    val status: StatusEntity
)

data class PeriodsEntity(
    val first: Long?,
    val second: Long?
)

data class VenueEntity(
    val id: Int?,
    val name: String?,
    val city: String?
)

data class LeagueEntity(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String?,
    val flag: String?,
    val season: Int,
    val round: String
)

data class TeamsEntity(
    val home: TeamEntity,
    val away: TeamEntity
)

data class TeamEntity(
    val id: Int,
    val name: String,
    val logo: String?,
    val winner: Boolean?
)

data class GoalsEntity(
    val home: Int?,
    val away: Int?
)

data class ScoreEntity(
    val halftime: GoalsEntity?,
    val fulltime: GoalsEntity?,
    val extratime: GoalsEntity?,
    val penalty: GoalsEntity?
)

data class StatusEntity(
    val long: String,
    val short: String,
    val elapsed: Int?,
    val isLive : Boolean,
    val hasStarted : Boolean
)

// Detailed Fixture Entities
data class DetailedFixtureEntity(
    val fixture: FixtureDetailEntity,
    val league: LeagueEntity,
    val teams: DetailedTeamsEntity,
    val goals: GoalsEntity,
    val score: ScoreEntity,
    val events: List<EventEntity>?,
    val lineups: LineupsEntity?,
    val statistics: List<StatisticsEntity>?,
    val players: PlayersEntity?
)

data class DetailedTeamsEntity(
    val home: DetailedTeamEntity,
    val away: DetailedTeamEntity
)

data class DetailedTeamEntity(
    val id: Int,
    val name: String,
    val logo: String?,
    val winner: Boolean?,
    val colors: TeamColorsEntity?
)

data class TeamColorsEntity(
    val player: TeamColorEntity?,
    val goalkeeper: TeamColorEntity?
)

data class TeamColorEntity(
    val primary: String?,
    val number: String?,
    val border: String?
)

// Event Entities
data class EventEntity(
    val time: EventTimeEntity,
    val team: EventTeamEntity,
    val player: EventPlayerEntity,
    val assist: EventPlayerEntity?,
    val type: String,
    val detail: String,
    val comments: String?
)

data class EventTimeEntity(
    val elapsed: Int?,
    val extra: Int?
)

data class EventTeamEntity(
    val id: Int,
    val name: String,
    val logo: String?,
    val colors: TeamColorsEntity?
)

data class EventPlayerEntity(
    val id: Int,
    val name: String
)

// Lineups Entities
data class LineupsEntity(
    val home: LineupEntity?,
    val away: LineupEntity?
)

data class LineupEntity(
    val team: LineupTeamEntity,
    val formation: String,
    val startXI: List<LineupPlayerEntity>,
    val substitutes: List<LineupPlayerEntity>,
    val coach: CoachEntity,
    val colors: TeamColorsEntity?
)

data class LineupTeamEntity(
    val id: Int,
    val name: String,
    val logo: String?,
    val colors: TeamColorsEntity?
)

data class LineupPlayerEntity(
    val player: PlayerEntity
)

data class PlayerEntity(
    val id: Int,
    val name: String,
    val number: Int,
    val pos: String,
    val grid: String?
)

data class CoachEntity(
    val id: Int,
    val name: String,
    val photo: String?
)

// Statistics Entities
data class StatisticsEntity(
    val team: StatisticsTeamEntity,
    val statistics: List<StatisticEntity>
)

data class StatisticsTeamEntity(
    val id: Int,
    val name: String,
    val logo: String?
)

data class StatisticEntity(
    val type: String,
    val value: Any?
)

// Players Entities
data class PlayersEntity(
    val home: List<PlayerDataEntity>?,
    val away: List<PlayerDataEntity>?
)

data class PlayerDataEntity(
    val team: PlayerTeamEntity,
    val players: List<PlayerInfoEntity>
)

data class PlayerTeamEntity(
    val id: Int,
    val name: String,
    val logo: String?,
    val update: String
)

data class PlayerInfoEntity(
    val player: PlayerDetailsEntity,
    val statistics: List<PlayerStatisticEntity>
)

data class PlayerDetailsEntity(
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

data class PlayerStatisticEntity(
    val team: PlayerTeamEntity,
    val league: PlayerLeagueEntity,
    val games: PlayerGamesEntity,
    val substitutes: PlayerSubstitutesEntity,
    val shots: PlayerShotsEntity,
    val goals: PlayerGoalsEntity,
    val passes: PlayerPassesEntity,
    val tackles: PlayerTacklesEntity,
    val duels: PlayerDuelsEntity,
    val dribbles: PlayerDribblesEntity,
    val fouls: PlayerFoulsEntity,
    val cards: PlayerCardsEntity,
    val penalty: PlayerPenaltyEntity
)

data class PlayerLeagueEntity(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String?,
    val flag: String?,
    val season: Int
)

data class PlayerGamesEntity(
    val appearances: Int?,
    val lineups: Int?,
    val minutes: Int?,
    val number: Int?,
    val position: String?,
    val rating: String?,
    val captain: Boolean?
)

data class PlayerSubstitutesEntity(
    val inValue: Int?,
    val out: Int?,
    val bench: Int?
)

data class PlayerShotsEntity(
    val total: Int?,
    val on: Int?
)

data class PlayerGoalsEntity(
    val total: Int?,
    val conceded: Int?,
    val assists: Int?,
    val saves: Int?
)

data class PlayerPassesEntity(
    val total: Int?,
    val key: Int?,
    val accuracy: String?
)

data class PlayerTacklesEntity(
    val total: Int?,
    val blocks: Int?,
    val interceptions: Int?
)

data class PlayerDuelsEntity(
    val total: Int?,
    val won: Int?
)

data class PlayerDribblesEntity(
    val attempts: Int?,
    val success: Int?,
    val past: Int?
)

data class PlayerFoulsEntity(
    val drawn: Int?,
    val committed: Int?
)

data class PlayerCardsEntity(
    val yellow: Int?,
    val red: Int?
)

data class PlayerPenaltyEntity(
    val won: Int?,
    val committed: Int?,
    val scored: Int?,
    val missed: Int?,
    val saved: Int?
)

