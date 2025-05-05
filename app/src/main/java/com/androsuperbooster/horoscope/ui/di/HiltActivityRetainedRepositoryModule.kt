package com.androsuperbooster.horoscope.ui.di

import com.androsuperbooster.horoscope.data.repo.CurrenciesRepositoryImpl
import com.androsuperbooster.horoscope.data.repo.InstallReferrerRepositoryImpl
import com.androsuperbooster.horoscope.data.repo.SettingsRepositoryImpl
import com.androsuperbooster.horoscope.data.repo.ZodiacRepositoryImpl
import com.androsuperbooster.horoscope.domain.repo.CurrenciesRepository
import com.androsuperbooster.horoscope.domain.repo.InstallReferrerRepository
import com.androsuperbooster.horoscope.domain.repo.SettingsRepository
import com.androsuperbooster.horoscope.domain.repo.ZodiacRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ActivityRepositoryModule {

    @Binds
    abstract fun bindCurrenciesRepository(repository: CurrenciesRepositoryImpl): CurrenciesRepository

    @Binds
    abstract fun bindZodiacRepository(repository: ZodiacRepositoryImpl): ZodiacRepository

    @Binds
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindInstallReferrerRepository(repository: InstallReferrerRepositoryImpl): InstallReferrerRepository

}