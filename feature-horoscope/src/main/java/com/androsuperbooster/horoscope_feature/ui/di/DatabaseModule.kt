package com.androsuperbooster.horoscope_feature.ui.di

import android.content.Context
import androidx.room.Room
import com.androsuperbooster.horoscope_feature.data.sources.database.AppDatabase
import com.androsuperbooster.horoscope_feature.data.sources.database.dao.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class DatabaseModule {

    @Singleton
    @Provides
    fun providesApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    open fun providesDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context = context.applicationContext,
                klass = AppDatabase::class.java,
                name = "base_db"
            )
            .build()
    }

    @Singleton
    @Provides
    fun providesCurrenciesDao(dataBase: AppDatabase): CurrencyDao =
        dataBase.getCurrencyDao()
}