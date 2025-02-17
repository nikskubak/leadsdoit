package com.respire.core_features.consent

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.util.concurrent.atomic.AtomicBoolean

object ConsentManager {

    private var consentInformation: ConsentInformation? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

     fun initAdsWithConsent(activity: Activity, onAdsInitSuccess: () -> Unit) {

        //for debug
//        val debugSettings = ConsentDebugSettings.Builder(this)
//            .addTestDeviceHashedId("26E7D222E58EE572F1E5CEE007C8D8E8")
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .build()


        val params = ConsentRequestParameters
            .Builder()
//            .setConsentDebugSettings(debugSettings)
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation?.requestConsentInfoUpdate(
            activity,
            params,
            {
                Log.e(
                    "checkGDPR", "Show consent form"
                )
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    // Consent gathering failed.
                    Log.e(
                        "checkGDPR", String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    // Consent has been gathered.
                    if (consentInformation?.canRequestAds() == true) {
                        initializeMobileAdsSdk(activity, onAdsInitSuccess)
                    }
                }
            },
            { requestConsentError ->
                // Consent gathering failed.
                Log.e(
                    "checkGDPR", String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
                // Check if you can initialize the Google Mobile Ads SDK in parallel
                // while checking for new consent information. Consent obtained in
                // the previous session can be used to request ads.
                if (consentInformation?.canRequestAds() == true) {
                    initializeMobileAdsSdk(activity, onAdsInitSuccess)
                }
            })

        //for debug
//        consentInformation?.reset()
    }

    private fun initializeMobileAdsSdk(activity: Activity, onAdsInitSuccess: () -> Unit) {
        Log.e("loadAd", "initializeMobileAdsSdk")
        isMobileAdsInitializeCalled.set(true)
        var requestConfiguration = MobileAds.getRequestConfiguration()
            .toBuilder()
            .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)
        MobileAds.initialize(activity)
        onAdsInitSuccess()
    }
}