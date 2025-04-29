package com.androsuperbooster.horoscope.ui.base

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)
        Hawk.init(this).build()
        FirebaseInAppMessaging.getInstance().setMessagesSuppressed(true)
    }
}