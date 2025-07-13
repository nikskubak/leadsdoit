package com.respire.mvi.ui.di

import com.respire.mvi.data.repository.FootballMatchesRepositoryImpl
import com.respire.mvi.domain.repository.FootballMatchesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ActivityRepositoryModule {

    @Binds
    abstract fun bindFootballMatchesRepository(repository: FootballMatchesRepositoryImpl): FootballMatchesRepository

}