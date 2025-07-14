package com.respire.mvi.domain.useCase

import com.respire.mvi.domain.model.FixtureEntity
import com.respire.mvi.domain.repository.FootballMatchesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FootballMatchesUseCase @Inject constructor(private val footballMatchesRepository: FootballMatchesRepository) {
    operator fun invoke(date: String): Flow<Result<List<FixtureEntity>>> {
        return footballMatchesRepository.getMatches(date)
    }
}