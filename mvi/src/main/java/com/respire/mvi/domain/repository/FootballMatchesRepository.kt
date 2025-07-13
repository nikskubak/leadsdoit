package com.respire.mvi.domain.repository

import com.respire.mvi.domain.model.FixtureEntity
import kotlinx.coroutines.flow.Flow

interface FootballMatchesRepository {
    fun getMatches(date : String): Flow<Result<List<FixtureEntity>>>
}