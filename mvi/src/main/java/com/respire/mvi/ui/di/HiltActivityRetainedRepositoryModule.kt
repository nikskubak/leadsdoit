package com.respire.mvi.ui.di

import com.respire.mvi.data.repository.QuizRepositoryImpl
import com.respire.mvi.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ActivityRepositoryModule {

    @Binds
    abstract fun bindQuizRepository(repository: QuizRepositoryImpl): QuizRepository

}