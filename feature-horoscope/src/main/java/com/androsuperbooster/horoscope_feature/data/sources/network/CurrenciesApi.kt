package com.androsuperbooster.horoscope_feature.data.sources.network

//import com.androsuperbooster.horoscope_feature.BuildConfig
import com.androsuperbooster.horoscope_feature.data.sources.network.models.response.CurrenciesMapResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

interface CurrenciesApi {
    @GET("v1/cryptocurrency/map")
    @Headers("X-CMC_PRO_API_KEY: ${"BuildConfig.API_KEY"}")
    suspend fun getCurrencies(@HeaderMap options: Map<String, String>): Response<CurrenciesMapResponse>
}