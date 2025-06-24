package com.androsuperbooster.horoscope_feature.ui.di

import android.content.Context
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.androsuperbooster.horoscope_feature.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class FirebaseModule {

    @Singleton
    @Provides
    open fun providesFirebaseRemoteConfig(context: Context): FirebaseRemoteConfig {
        Log.e("abcd", "providesFirebase")
        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
        return remoteConfig
    }
}