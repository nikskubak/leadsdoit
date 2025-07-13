package com.respire.mvi.ui.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.respire.mvi.BuildConfig
import com.respire.mvi.data.dataSource.network.FootballMatchesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    @Named("module")
    fun providesMatchesRetrofit(
        @Named("module") baseUrl: String,
        @Named("module") client: OkHttpClient,
        @Named("module") converter: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(converter))
            .build()
    }

    @Singleton
    @Provides
    @Named("module")
    fun provideOkHttpClient(
        @Named("module") httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("module")
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    @Named("module")
    fun provideGsonConverter(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    @Named("module")
    fun providesUrl() = BuildConfig.URL

    @Singleton
    @Provides
    fun providesFootballMatchesApi(
        @Named("module") retrofit: Retrofit
    ): FootballMatchesApi = retrofit.create(FootballMatchesApi::class.java)

}