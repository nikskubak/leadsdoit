package com.androsuperbooster.horoscope.ui.di

import com.androsuperbooster.horoscope.data.repo.CurrenciesRepositoryImpl
import com.androsuperbooster.horoscope.data.repo.SettingsRepositoryImpl
import com.androsuperbooster.horoscope.data.repo.ZodiacRepositoryImpl
import com.androsuperbooster.horoscope.domain.repo.CurrenciesRepository
import com.androsuperbooster.horoscope.domain.repo.SettingsRepository
import com.androsuperbooster.horoscope.domain.repo.ZodiacRepository
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