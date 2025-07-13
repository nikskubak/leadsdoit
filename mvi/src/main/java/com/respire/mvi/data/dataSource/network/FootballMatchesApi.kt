package com.respire.mvi.data.dataSource.network

import com.respire.mvi.BuildConfig
import com.respire.mvi.data.dataSource.network.models.response.FootballMatchesResponse
import com.respire.mvi.data.dataSource.network.models.response.FixtureResponse
import com.respire.mvi.data.dataSource.network.models.response.DetailedFixtureResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FootballMatchesApi {

    /**
     * Get live fixtures for a specific date
     * @param date Date in YYYY-MM-DD format (default: today)
     * @param league League ID (optional)
     * @param season Season year (optional)
     * @param team Team ID (optional)
     * @param last Number of last fixtures (optional)
     * @param next Number of next fixtures (optional)
     * @param from Start date in YYYY-MM-DD format (optional)
     * @param to End date in YYYY-MM-DD format (optional)
     * @param round Round of the league (optional)
     * @param status Status of the fixture (optional)
     */
    @GET("fixtures")
    @Headers(
        "x-rapidapi-key:${BuildConfig.API_KEY}",
        "x-rapidapi-host:${BuildConfig.API_HOST}"
    )
    suspend fun getFixtures(
        @Query("date") date: String? = null,
        @Query("league") league: Int? = null,
        @Query("season") season: Int? = null,
        @Query("team") team: Int? = null,
        @Query("last") last: Int? = null,
        @Query("next") next: Int? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("round") round: String? = null,
        @Query("status") status: String? = null
    ): Response<FootballMatchesResponse<FixtureResponse>>

    /**
     * Get live fixtures (fixtures happening now)
     */
    @GET("v3/fixtures")
    suspend fun getLiveFixtures(
        @Query("live") live: String = "all"
    ): Response<FootballMatchesResponse<FixtureResponse>>

    /**
     * Get fixtures for today (default date)
     */
    @GET("v3/fixtures")
    suspend fun getTodayFixtures(): Response<FootballMatchesResponse<FixtureResponse>>

    /**
     * Get detailed fixture information by ID
     * @param id Fixture ID
     */
    @GET("v3/fixtures")
    suspend fun getFixtureById(
        @Query("id") id: Int
    ): Response<FootballMatchesResponse<DetailedFixtureResponse>>

    /**
     * Get fixtures with detailed information
     * @param date Date in YYYY-MM-DD format (default: today)
     * @param league League ID (optional)
     * @param season Season year (optional)
     * @param team Team ID (optional)
     * @param last Number of last fixtures (optional)
     * @param next Number of next fixtures (optional)
     * @param from Start date in YYYY-MM-DD format (optional)
     * @param to End date in YYYY-MM-DD format (optional)
     * @param round Round of the league (optional)
     * @param status Status of the fixture (optional)
     */
    @GET("v3/fixtures")
    suspend fun getDetailedFixtures(
        @Query("date") date: String? = null,
        @Query("league") league: Int? = null,
        @Query("season") season: Int? = null,
        @Query("team") team: Int? = null,
        @Query("last") last: Int? = null,
        @Query("next") next: Int? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("round") round: String? = null,
        @Query("status") status: String? = null
    ): Response<FootballMatchesResponse<DetailedFixtureResponse>>

    /**
     * Get fixtures for a specific date range
     * @param from Start date in YYYY-MM-DD format
     * @param to End date in YYYY-MM-DD format
     * @param league League ID (optional)
     * @param season Season year (optional)
     * @param team Team ID (optional)
     */
    @GET("v3/fixtures")
    suspend fun getFixturesByDateRange(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("league") league: Int? = null,
        @Query("season") season: Int? = null,
        @Query("team") team: Int? = null
    ): Response<FootballMatchesResponse<FixtureResponse>>

    /**
     * Get fixtures for a specific team
     * @param team Team ID
     * @param season Season year (optional)
     * @param last Number of last fixtures (optional)
     * @param next Number of next fixtures (optional)
     */
    @GET("v3/fixtures")
    suspend fun getTeamFixtures(
        @Query("team") team: Int,
        @Query("season") season: Int? = null,
        @Query("last") last: Int? = null,
        @Query("next") next: Int? = null
    ): Response<FootballMatchesResponse<FixtureResponse>>

    /**
     * Get fixtures for a specific league
     * @param league League ID
     * @param season Season year (optional)
     * @param round Round of the league (optional)
     */
    @GET("v3/fixtures")
    suspend fun getLeagueFixtures(
        @Query("league") league: Int,
        @Query("season") season: Int? = null,
        @Query("round") round: String? = null
    ): Response<FootballMatchesResponse<FixtureResponse>>
}