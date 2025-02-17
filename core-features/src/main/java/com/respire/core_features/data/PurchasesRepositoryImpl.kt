package com.respire.core_features.data

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Pair
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.orhanobut.hawk.Hawk
import com.respire.core_features.NetworkUtils
import com.respire.core_features.billing.BillingManager
import com.respire.core_features.domain.PurchasesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PurchasesRepositoryImpl @Inject constructor(
    var context: Context
) : PurchasesRepository {

    var newBillingManager: BillingManager? = null

    override fun getPurchasesFromGooglePlay(
        activity: Activity,
        coroutineScope: CoroutineScope
    ): Flow<Pair<Result<List<PurchaseHistoryRecord>?>?, Result<MutableList<Purchase>?>?>>? {
        newBillingManager =
            BillingManager.Companion.Builder(activity)
                .build()
        return newBillingManager?.getConsumedPurchasesFlow(ProductType.INAPP, coroutineScope)
            ?.flatMapConcat { inapp ->
                newBillingManager?.getActivePurchasesFlow(
                    ProductType.SUBS,
                    coroutineScope
                )!!.map { subs ->
                    Log.e(
                        "isProMode inapp",
                        "inapp ${inapp?.getOrNull()?.size ?: 0} - subs ${subs?.getOrNull()?.size ?: 0}"
                    )
                    Pair.create(inapp, subs)
                }
            }
    }


    override fun isProMode(
        activity: Activity,
        coroutineScope: CoroutineScope
    ): Flow<Result<Boolean>>? {
        val localPurchases = Hawk.get<MutableList<Purchase>?>(HawkConstants.PURCHASES)
        if (localPurchases.isNullOrEmpty()) {
            if (NetworkUtils.isInternetAvailable(activity)) {
                return getPurchasesFromGooglePlay(activity, coroutineScope)
                    ?.map {
                        if (!it.first?.getOrNull().isNullOrEmpty()) {
                            it.first?.getOrDefault(mutableListOf())?.let { list ->
                                if (list.isNotEmpty()) {
                                    Hawk.put(HawkConstants.PURCHASES, list)
                                }
                            }
                        } else {
                            Hawk.put(HawkConstants.SUBS, it.second?.getOrNull())
                        }
                        val isProModeResult =
                            if (!it.first?.getOrNull().isNullOrEmpty() || !it.second?.getOrNull()
                                    .isNullOrEmpty()
                            ) {
                                Result.success(true)
                            } else {
                                Result.failure(Exception("Purchases not found"))
                            }
                        return@map isProModeResult
                    }
                    ?.catch {
                        Log.e("catch", it.toString())
                        emit(Result.failure(Exception("Purchases not found")))
                    }
            } else {
                val localSubs = Hawk.get<MutableList<Purchase>?>(HawkConstants.SUBS)
                if (localSubs.isNullOrEmpty()) {
                    return flowOf(Result.failure(Exception("Purchases not found")))
                } else {
                    Log.e("isProMode", "localSubs = ${localSubs.size}")
                    return flowOf(Result.success(true))
                }
            }
        } else {
            Log.e("localPurchases", localPurchases.toString())
            return flowOf(Result.success(true))
        }
    }
}