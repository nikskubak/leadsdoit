package com.respire.core_features.consent

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.respire.core_features.ads.AdsManager
import java.util.concurrent.atomic.AtomicBoolean

class GoogleAdsManager : AdsManager {

    companion object {
        const val TAG = "GoogleAdsManager"
    }

    private var consentInformation: ConsentInformation? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

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

    override fun initAds(activity: Activity, onAdsInitSuccess: () -> Unit) {

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

    override fun loadFullScreenBanner(id: String) {

    }

    override fun showRewardedFullScreenBanner(
        id: String,
        context: Context,
        activity: Activity,
        onRewardReceived: (rewardItem: RewardItem?) -> Unit
    ) {
        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, id, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                onRewardReceived(null)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                var rewardedAd : RewardedAd? = ad
                var rewardItem : RewardItem? = null
                rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Ad dismissed fullscreen content.")
                        rewardedAd = null
                        onRewardReceived(rewardItem)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.")
                        rewardedAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.")
                    }
                }
                rewardedAd?.let { ad ->
                    ad.show(activity) { reward ->
                        rewardItem = reward
                        Log.d(TAG, "User earned the reward.")
                    }
                } ?: run {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.")
                }
            }
        })
    }
}