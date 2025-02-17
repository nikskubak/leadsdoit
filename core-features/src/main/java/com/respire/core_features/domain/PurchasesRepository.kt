package com.respire.core_features.domain

import android.app.Activity
import android.util.Pair
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface PurchasesRepository {
    fun getPurchasesFromGooglePlay(
        activity: Activity,
        coroutineScope: CoroutineScope
    ): Flow<Pair<Result<List<PurchaseHistoryRecord>?>?, Result<MutableList<Purchase>?>?>>?

    fun isProMode(activity: Activity, coroutineScope: CoroutineScope): Flow<Result<Boolean>>?
}