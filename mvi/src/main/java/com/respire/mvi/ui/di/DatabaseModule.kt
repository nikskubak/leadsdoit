package com.respire.mvi.ui.di

import android.content.Context
import androidx.room.Room
import com.respire.mvi.data.dataSource.database.FootballDatabase
import com.respire.mvi.data.dataSource.database.dao.MatchesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class DataModule {

    @Singleton
    @Provides
    @Named("appContext")
    fun providesApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    open fun providesFootballDatabase(context: Context): FootballDatabase {
        return Room
            .databaseBuilder(
                context = context.applicationContext,
                klass = FootballDatabase::class.java,
                name = "football_matches_db"
            )
            .build()
    }

    @Singleton
    @Provides
    fun providesMatchesDao(dataBase: FootballDatabase): MatchesDao =
        dataBase.getMatchesDao()

}