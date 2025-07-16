package com.respire.mvi.data.dataSource.network.models.mapper

import android.util.Log
import com.respire.mvi.data.dataSource.network.models.response.*
import com.respire.mvi.domain.model.*
import java.text.SimpleDateFormat
import java.util.Locale

object FootballMatchesMapper {
    
    // Main response mapper
    fun <T, R> mapResponse(
        response: FootballMatchesResponse<T>,
        itemMapper: (T) -> R
    ): FootballMatchesEntities<R> {
        return FootballMatchesEntities(
            get = response.get,
            parameters = response.parameters,
            errors = response.errors,
            results = response.results,
            paging = response.paging?.toEntity(),
            response = response.response.map(itemMapper)
        )
    }
    
    // Paging mapper
    fun Paging.toEntity(): PagingEntity {
        return PagingEntity(
            current = current,
            total = total
        )
    }
    
    // Fixture response mapper
    fun FixtureResponse.toEntity(): FixtureEntity {
        return FixtureEntity(
            fixture = fixture.toEntity(),
            league = league.toEntity(),
            teams = teams.toEntity(),
            goals = goals.toEntity(),
            score = score.toEntity()
        )
    }
    
    // Fixture detail mapper
    fun Fixture.toEntity(): FixtureDetailEntity {
        return FixtureDetailEntity(
            id = id,
            referee = referee,
            timezone = timezone,
            date = formatDate(date),
            time = formatTime(date),
            timestamp = timestamp,
            periods = periods.toEntity(),
            venue = venue?.toEntity(),
            status = status.toEntity()
        )
    }
    
    // Periods mapper
    fun Periods.toEntity(): PeriodsEntity {
        return PeriodsEntity(
            first = first,
            second = second
        )
    }
    
    // Venue mapper
    fun Venue.toEntity(): VenueEntity {
        return VenueEntity(
            id = id,
            name = name,
            city = city
        )
    }
    
    // Status mapper
    fun Status.toEntity(): StatusEntity {
        val isLive = short == "LIVE" ||
                short == "HT" ||
                short == "1H" ||
                short == "2H"

        val hasStarted = isLive ||
                short == "FT" ||
                short == "AET" ||
                short == "PEN" ||
                short == "PST" ||
                short == "CANC" ||
                short == "ABD" ||
                short == "AWD" ||
                short == "WO"
        return StatusEntity(
            long = long,
            short = short,
            elapsed = elapsed,
            isLive = isLive,
            hasStarted = hasStarted
        )
    }
    
    // League mapper
    fun League.toEntity(): LeagueEntity {
        return LeagueEntity(
            id = id,
            name = name,
            country = country,
            logo = logo,
            flag = flag,
            season = season,
            round = round
        )
    }
    
    // Teams mapper
    fun Teams.toEntity(): TeamsEntity {
        return TeamsEntity(
            home = home.toEntity(),
            away = away.toEntity()
        )
    }
    
    // Team mapper
    fun Team.toEntity(): TeamEntity {
        return TeamEntity(
            id = id,
            name = name,
            logo = logo,
            winner = winner
        )
    }
    
    // Goals mapper
    fun Goals.toEntity(): GoalsEntity {
        return GoalsEntity(
            home = home,
            away = away
        )
    }
    
    // Score mapper
    fun Score.toEntity(): ScoreEntity {
        return ScoreEntity(
            halftime = halftime?.toEntity(),
            fulltime = fulltime?.toEntity(),
            extratime = extratime?.toEntity(),
            penalty = penalty?.toEntity()
        )
    }
    
    // Detailed fixture response mapper
    fun DetailedFixtureResponse.toEntity(): DetailedFixtureEntity {
        return DetailedFixtureEntity(
            fixture = fixture.toEntity(),
            league = league.toEntity(),
            teams = teams.toEntity(),
            goals = goals.toEntity(),
            score = score.toEntity(),
            events = events?.map { it.toEntity() },
            lineups = lineups?.toEntity(),
            statistics = statistics?.map { it.toEntity() },
            players = players?.toEntity()
        )
    }
    
    // Detailed fixture mapper
    fun DetailedFixture.toEntity(): FixtureDetailEntity {
        return FixtureDetailEntity(
            id = id,
            referee = referee,
            timezone = timezone,
            date = formatDate(date),
            time = formatTime(date),
            timestamp = timestamp,
            periods = periods.toEntity(),
            venue = venue?.toEntity(),
            status = status.toEntity()
        )
    }
    
    // Detailed league mapper
    fun DetailedLeague.toEntity(): LeagueEntity {
        return LeagueEntity(
            id = id,
            name = name,
            country = country,
            logo = logo,
            flag = flag,
            season = season,
            round = round
        )
    }
    
    // Detailed teams mapper
    fun DetailedTeams.toEntity(): DetailedTeamsEntity {
        return DetailedTeamsEntity(
            home = home.toEntity(),
            away = away.toEntity()
        )
    }
    
    // Detailed team mapper
    fun DetailedTeam.toEntity(): DetailedTeamEntity {
        return DetailedTeamEntity(
            id = id,
            name = name,
            logo = logo,
            winner = winner,
            colors = colors?.toEntity()
        )
    }
    
    // Team colors mapper
    fun TeamColors.toEntity(): TeamColorsEntity {
        return TeamColorsEntity(
            player = player?.toEntity(),
            goalkeeper = goalkeeper?.toEntity()
        )
    }
    
    // Team color mapper
    fun TeamColor.toEntity(): TeamColorEntity {
        return TeamColorEntity(
            primary = primary,
            number = number,
            border = border
        )
    }
    
    // Detailed goals mapper
    fun DetailedGoals.toEntity(): GoalsEntity {
        return GoalsEntity(
            home = home,
            away = away
        )
    }
    
    // Detailed score mapper
    fun DetailedScore.toEntity(): ScoreEntity {
        return ScoreEntity(
            halftime = halftime?.toEntity(),
            fulltime = fulltime?.toEntity(),
            extratime = extratime?.toEntity(),
            penalty = penalty?.toEntity()
        )
    }
    
    // Event mapper
    fun Event.toEntity(): EventEntity {
        return EventEntity(
            time = time.toEntity(),
            team = team.toEntity(),
            player = player.toEntity(),
            assist = assist?.toEntity(),
            type = type,
            detail = detail,
            comments = comments
        )
    }
    
    // Event time mapper
    fun EventTime.toEntity(): EventTimeEntity {
        return EventTimeEntity(
            elapsed = elapsed,
            extra = extra
        )
    }
    
    // Event team mapper
    fun EventTeam.toEntity(): EventTeamEntity {
        return EventTeamEntity(
            id = id,
            name = name,
            logo = logo,
            colors = colors?.toEntity()
        )
    }
    
    // Event player mapper
    fun EventPlayer.toEntity(): EventPlayerEntity {
        return EventPlayerEntity(
            id = id,
            name = name
        )
    }
    
    // Lineups mapper
    fun Lineups.toEntity(): LineupsEntity {
        return LineupsEntity(
            home = home?.toEntity(),
            away = away?.toEntity()
        )
    }
    
    // Lineup mapper
    fun Lineup.toEntity(): LineupEntity {
        return LineupEntity(
            team = team.toEntity(),
            formation = formation,
            startXI = startXI.map { it.toEntity() },
            substitutes = substitutes.map { it.toEntity() },
            coach = coach.toEntity(),
            colors = colors?.toEntity()
        )
    }
    
    // Lineup team mapper
    fun LineupTeam.toEntity(): LineupTeamEntity {
        return LineupTeamEntity(
            id = id,
            name = name,
            logo = logo,
            colors = colors?.toEntity()
        )
    }
    
    // Lineup player mapper
    fun LineupPlayer.toEntity(): LineupPlayerEntity {
        return LineupPlayerEntity(
            player = player.toEntity()
        )
    }
    
    // Player mapper
    fun Player.toEntity(): PlayerEntity {
        return PlayerEntity(
            id = id,
            name = name,
            number = number,
            pos = pos,
            grid = grid
        )
    }
    
    // Coach mapper
    fun Coach.toEntity(): CoachEntity {
        return CoachEntity(
            id = id,
            name = name,
            photo = photo
        )
    }
    
    // Statistics mapper
    fun Statistics.toEntity(): StatisticsEntity {
        return StatisticsEntity(
            team = team.toEntity(),
            statistics = statistics.map { it.toEntity() }
        )
    }
    
    // Statistics team mapper
    fun StatisticsTeam.toEntity(): StatisticsTeamEntity {
        return StatisticsTeamEntity(
            id = id,
            name = name,
            logo = logo
        )
    }
    
    // Statistic mapper
    fun Statistic.toEntity(): StatisticEntity {
        return StatisticEntity(
            type = type,
            value = value
        )
    }
    
    // Players mapper
    fun Players.toEntity(): PlayersEntity {
        return PlayersEntity(
            home = home?.map { it.toEntity() },
            away = away?.map { it.toEntity() }
        )
    }
    
    // Player data mapper
    fun PlayerData.toEntity(): PlayerDataEntity {
        return PlayerDataEntity(
            team = team.toEntity(),
            players = players.map { it.toEntity() }
        )
    }
    
    // Player team mapper
    fun PlayerTeam.toEntity(): PlayerTeamEntity {
        return PlayerTeamEntity(
            id = id,
            name = name,
            logo = logo,
            update = update
        )
    }
    
    // Player info mapper
    fun PlayerInfo.toEntity(): PlayerInfoEntity {
        return PlayerInfoEntity(
            player = player.toEntity(),
            statistics = statistics.map { it.toEntity() }
        )
    }
    
    // Player details mapper
    fun PlayerDetails.toEntity(): PlayerDetailsEntity {
        return PlayerDetailsEntity(
            id = id,
            name = name,
            firstname = firstname,
            lastname = lastname,
            age = age,
            nationality = nationality,
            height = height,
            weight = weight,
            injured = injured,
            photo = photo
        )
    }
    
    // Player statistic mapper
    fun PlayerStatistic.toEntity(): PlayerStatisticEntity {
        return PlayerStatisticEntity(
            team = team.toEntity(),
            league = league.toEntity(),
            games = games.toEntity(),
            substitutes = substitutes.toEntity(),
            shots = shots.toEntity(),
            goals = goals.toEntity(),
            passes = passes.toEntity(),
            tackles = tackles.toEntity(),
            duels = duels.toEntity(),
            dribbles = dribbles.toEntity(),
            fouls = fouls.toEntity(),
            cards = cards.toEntity(),
            penalty = penalty.toEntity()
        )
    }
    
    // Player league mapper
    fun PlayerLeague.toEntity(): PlayerLeagueEntity {
        return PlayerLeagueEntity(
            id = id,
            name = name,
            country = country,
            logo = logo,
            flag = flag,
            season = season
        )
    }
    
    // Player games mapper
    fun PlayerGames.toEntity(): PlayerGamesEntity {
        return PlayerGamesEntity(
            appearances = appearances,
            lineups = lineups,
            minutes = minutes,
            number = number,
            position = position,
            rating = rating,
            captain = captain
        )
    }
    
    // Player substitutes mapper
    fun PlayerSubstitutes.toEntity(): PlayerSubstitutesEntity {
        return PlayerSubstitutesEntity(
            inValue = inValue,
            out = out,
            bench = bench
        )
    }
    
    // Player shots mapper
    fun PlayerShots.toEntity(): PlayerShotsEntity {
        return PlayerShotsEntity(
            total = total,
            on = on
        )
    }
    
    // Player goals mapper
    fun PlayerGoals.toEntity(): PlayerGoalsEntity {
        return PlayerGoalsEntity(
            total = total,
            conceded = conceded,
            assists = assists,
            saves = saves
        )
    }
    
    // Player passes mapper
    fun PlayerPasses.toEntity(): PlayerPassesEntity {
        return PlayerPassesEntity(
            total = total,
            key = key,
            accuracy = accuracy
        )
    }
    
    // Player tackles mapper
    fun PlayerTackles.toEntity(): PlayerTacklesEntity {
        return PlayerTacklesEntity(
            total = total,
            blocks = blocks,
            interceptions = interceptions
        )
    }
    
    // Player duels mapper
    fun PlayerDuels.toEntity(): PlayerDuelsEntity {
        return PlayerDuelsEntity(
            total = total,
            won = won
        )
    }
    
    // Player dribbles mapper
    fun PlayerDribbles.toEntity(): PlayerDribblesEntity {
        return PlayerDribblesEntity(
            attempts = attempts,
            success = success,
            past = past
        )
    }
    
    // Player fouls mapper
    fun PlayerFouls.toEntity(): PlayerFoulsEntity {
        return PlayerFoulsEntity(
            drawn = drawn,
            committed = committed
        )
    }
    
    // Player cards mapper
    fun PlayerCards.toEntity(): PlayerCardsEntity {
        return PlayerCardsEntity(
            yellow = yellow,
            red = red
        )
    }
    
    // Player penalty mapper
    fun PlayerPenalty.toEntity(): PlayerPenaltyEntity {
        return PlayerPenaltyEntity(
            won = won,
            committed = committed,
            scored = scored,
            missed = missed,
            saved = saved
        )
    }

    private val inputDateFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
    )
    private val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)

    private fun formatDate(date: String): String {
        for (format in inputDateFormats) {
            try {
                val parsed = format.parse(date)
                if (parsed != null) {
                    val format1 = outputDateFormat.format(parsed)
                    Log.d("TAG", "formatDate: $date - $format1")
                    return format1
                }
            } catch (_: Exception) {}
        }
        return date // fallback to original if parsing fails
    }

    private fun formatTime(date: String): String {
        for (format in inputDateFormats) {
            try {
                val parsed = format.parse(date)
                if (parsed != null) {
                    val format1 = outputTimeFormat.format(parsed)
                    Log.d("TAG", "formatDate: $date - $format1")
                    return format1
                }
            } catch (_: Exception) {}
        }
        return date // fallback to original if parsing fails
    }
} 