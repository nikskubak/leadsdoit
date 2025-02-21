package com.respire.core_features.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd

interface AdsManager {
    fun initAds(activity: Activity, onAdsInitSuccess: () -> Unit)
    fun loadFullScreenBanner(id: String)
    fun showRewardedFullScreenBanner(
        id: String,
        context: Context,
        activity: Activity,
        onRewardReceived: (rewardItem: RewardItem?) -> Unit
    )
}