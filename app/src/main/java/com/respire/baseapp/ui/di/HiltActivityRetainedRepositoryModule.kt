package com.respire.baseapp.ui.di

import com.respire.baseapp.data.repo.CurrenciesRepositoryImpl
import com.respire.baseapp.data.repo.SettingsRepositoryImpl
import com.respire.baseapp.data.repo.ZodiacRepositoryImpl
import com.respire.baseapp.domain.repo.CurrenciesRepository
import com.respire.baseapp.domain.repo.SettingsRepository
import com.respire.baseapp.domain.repo.ZodiacRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class ActivityRepositoryModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindCurrenciesRepository(repository: CurrenciesRepositoryImpl): CurrenciesRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindZodiacRepository(repository: ZodiacRepositoryImpl): ZodiacRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

}