package com.respire.mvi.data.repository

import com.respire.mvi.data.dataSource.database.dao.MatchesDao
import com.respire.mvi.data.dataSource.database.models.DbMapper
import com.respire.mvi.data.dataSource.network.FootballMatchesApi
import com.respire.mvi.data.dataSource.network.models.mapper.FootballMatchesMapper
import com.respire.mvi.data.dataSource.network.models.mapper.FootballMatchesMapper.toEntity
import com.respire.mvi.domain.model.FixtureEntity
import com.respire.mvi.domain.repository.FootballMatchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FootballMatchesRepositoryImpl @Inject constructor(
    private val footballMatchesApi: FootballMatchesApi,
    private val matchesDao: MatchesDao
) : FootballMatchesRepository {
    
    override fun getMatches(date: String): Flow<Result<List<FixtureEntity>>> = flow {
        try {
            val response = footballMatchesApi.getFixtures(date = date)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = FootballMatchesMapper.mapResponse(apiResponse) { fixtureResponse ->
                        fixtureResponse.toEntity()
                    }
                    // Save to DB
                    val dbModels = entities.response.map { DbMapper.fromEntity(it) }
                    matchesDao.insertFixtures(dbModels)
                    emit(Result.success(entities.response))
                } ?: emit(Result.failure(Exception("Empty response from API")))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                // Try DB fallback
                val dbFixtures = matchesDao.getFixturesByDate(date).map { DbMapper.toEntity(it) }
                if (dbFixtures.isNotEmpty()) {
                    emit(Result.success(dbFixtures))
                } else {
                    emit(Result.failure(Exception("API Error: ${response.code()} - $errorMessage")))
                }
            }
        } catch (e: Exception) {
            // On network error, try DB fallback
            val dbFixtures = matchesDao.getFixturesByDate(date).map { DbMapper.toEntity(it) }
                .sortedBy { it.fixture.timestamp }
            if (dbFixtures.isNotEmpty()) {
                emit(Result.success(dbFixtures))
            } else {
                emit(Result.failure(Exception("Network error: ${e.message}")))
            }
        }
    }
    
    /**
     * Get matches for today (default date)
     */
    fun getTodayMatches(): Flow<Result<List<FixtureEntity>>> = flow {
        try {
            val response = footballMatchesApi.getTodayFixtures()
            
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = FootballMatchesMapper.mapResponse(apiResponse) { fixtureResponse ->
                        fixtureResponse.toEntity()
                    }
                    emit(Result.success(entities.response))
                } ?: emit(Result.failure(Exception("Empty response from API")))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                emit(Result.failure(Exception("API Error: ${response.code()} - $errorMessage")))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.message}")))
        }
    }
    
    /**
     * Get live matches (currently happening)
     */
    fun getLiveMatches(): Flow<Result<List<FixtureEntity>>> = flow {
        try {
            val response = footballMatchesApi.getLiveFixtures()
            
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = FootballMatchesMapper.mapResponse(apiResponse) { fixtureResponse ->
                        fixtureResponse.toEntity()
                    }
                    emit(Result.success(entities.response))
                } ?: emit(Result.failure(Exception("Empty response from API")))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                emit(Result.failure(Exception("API Error: ${response.code()} - $errorMessage")))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.message}")))
        }
    }
    
    /**
     * Get matches for a specific league
     */
    fun getLeagueMatches(leagueId: Int, season: Int? = null): Flow<Result<List<FixtureEntity>>> = flow {
        try {
            val response = footballMatchesApi.getLeagueFixtures(
                league = leagueId,
                season = season
            )
            
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = FootballMatchesMapper.mapResponse(apiResponse) { fixtureResponse ->
                        fixtureResponse.toEntity()
                    }
                    emit(Result.success(entities.response))
                } ?: emit(Result.failure(Exception("Empty response from API")))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                emit(Result.failure(Exception("API Error: ${response.code()} - $errorMessage")))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.message}")))
        }
    }
    
    /**
     * Get matches for a specific team
     */
    fun getTeamMatches(teamId: Int, season: Int? = null): Flow<Result<List<FixtureEntity>>> = flow {
        try {
            val response = footballMatchesApi.getTeamFixtures(
                team = teamId,
                season = season
            )
            
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = FootballMatchesMapper.mapResponse(apiResponse) { fixtureResponse ->
                        fixtureResponse.toEntity()
                    }
                    emit(Result.success(entities.response))
                } ?: emit(Result.failure(Exception("Empty response from API")))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                emit(Result.failure(Exception("API Error: ${response.code()} - $errorMessage")))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.message}")))
        }
    }
}