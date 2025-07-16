package com.respire.mvi.data.dataSource.database.models

import com.respire.mvi.domain.model.FixtureDetailEntity
import com.respire.mvi.domain.model.FixtureEntity
import com.respire.mvi.domain.model.GoalsEntity
import com.respire.mvi.domain.model.LeagueEntity
import com.respire.mvi.domain.model.PeriodsEntity
import com.respire.mvi.domain.model.ScoreEntity
import com.respire.mvi.domain.model.StatusEntity
import com.respire.mvi.domain.model.TeamEntity
import com.respire.mvi.domain.model.TeamsEntity
import com.respire.mvi.domain.model.VenueEntity

object DbMapper {

    fun fromEntity(entity: FixtureEntity): FixtureDB = FixtureDB(
        id = entity.fixture.id,
        fixture = FixtureDetailDB(
            referee = entity.fixture.referee,
            timezone = entity.fixture.timezone,
            date = entity.fixture.date,
            time = entity.fixture.time,
            timestamp = entity.fixture.timestamp,
            periods = PeriodsDB(
                first = entity.fixture.periods.first,
                second = entity.fixture.periods.second
            ),
            venue = entity.fixture.venue?.let {
                VenueDB(
                    id = it.id,
                    name = it.name,
                    city = it.city
                )
            },
            status = StatusDB(
                long = entity.fixture.status.long,
                short = entity.fixture.status.short,
                elapsed = entity.fixture.status.elapsed,
                isLive = entity.fixture.status.isLive,
                hasStarted = entity.fixture.status.hasStarted
            )
        ),
        league = LeagueDB(
            id = entity.league.id,
            name = entity.league.name,
            country = entity.league.country,
            logo = entity.league.logo,
            flag = entity.league.flag,
            season = entity.league.season,
            round = entity.league.round
        ),
        goals = GoalsDB(
            home = entity.goals.home,
            away = entity.goals.away
        ),
        score = ScoreDB(
            halftime = entity.score.halftime?.let { GoalsDB(it.home, it.away) },
            fulltime = entity.score.fulltime?.let { GoalsDB(it.home, it.away) },
            extratime = entity.score.extratime?.let { GoalsDB(it.home, it.away) },
            penalty = entity.score.penalty?.let { GoalsDB(it.home, it.away) }
        ),
        homeTeam = TeamDB(
            id = entity.teams.home.id,
            name = entity.teams.home.name,
            logo = entity.teams.home.logo,
            winner = entity.teams.home.winner
        ),
        awayTeam = TeamDB(
            id = entity.teams.away.id,
            name = entity.teams.away.name,
            logo = entity.teams.away.logo,
            winner = entity.teams.away.winner
        )
    )

    fun toEntity(db: FixtureDB): FixtureEntity = FixtureEntity(
        fixture = FixtureDetailEntity(
            id = db.id,
            referee = db.fixture.referee,
            timezone = db.fixture.timezone,
            date = db.fixture.date,
            time = db.fixture.time,
            timestamp = db.fixture.timestamp,
            periods = PeriodsEntity(
                first = db.fixture.periods.first,
                second = db.fixture.periods.second
            ),
            venue = db.fixture.venue?.let {
                VenueEntity(
                    id = it.id,
                    name = it.name,
                    city = it.city
                )
            },
            status = StatusEntity(
                long = db.fixture.status.long,
                short = db.fixture.status.short,
                elapsed = db.fixture.status.elapsed,
                isLive = db.fixture.status.isLive,
                hasStarted = db.fixture.status.hasStarted
            )
        ),
        league = LeagueEntity(
            id = db.league.id,
            name = db.league.name,
            country = db.league.country,
            logo = db.league.logo,
            flag = db.league.flag,
            season = db.league.season,
            round = db.league.round
        ),
        teams = TeamsEntity(
            home = TeamEntity(
                id = db.homeTeam.id,
                name = db.homeTeam.name,
                logo = db.homeTeam.logo,
                winner = db.homeTeam.winner
            ),
            away = TeamEntity(
                id = db.awayTeam.id,
                name = db.awayTeam.name,
                logo = db.awayTeam.logo,
                winner = db.awayTeam.winner
            )
        ),
        goals = GoalsEntity(
            home = db.goals.home,
            away = db.goals.away
        ),
        score = ScoreEntity(
            halftime = db.score.halftime?.let { GoalsEntity(it.home, it.away) },
            fulltime = db.score.fulltime?.let { GoalsEntity(it.home, it.away) },
            extratime = db.score.extratime?.let { GoalsEntity(it.home, it.away) },
            penalty = db.score.penalty?.let { GoalsEntity(it.home, it.away) }
        )
    )
} 