package com.respire.core_features.billing

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchaseHistory
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class BillingManager private constructor(var builder: Builder) {

    companion object {

        const val TAG = "BillingManager"

        class Builder(var activity: Activity) {
            var productsList: List<Product> = listOf()
            var onPurchaseSuccess: (purchase: Purchase) -> Unit = {}
            var onPurchaseFailed: (responseCode: Int) -> Unit = {}

            fun productsList(productsList: List<Product>) =
                apply { this.productsList = productsList }

            fun onPurchaseSuccessListener(onPurchaseSuccess: (purchase: Purchase) -> Unit) =
                apply { this.onPurchaseSuccess = onPurchaseSuccess }

            fun onPurchaseFailedListener(onPurchaseFailed: (responseCode: Int) -> Unit) =
                apply { this.onPurchaseFailed = onPurchaseFailed }

            fun build(): BillingManager {
                val billingManager = BillingManager(this)
                billingManager.init()
                return billingManager
            }
        }
    }

    private var billingClient: BillingClient? = null

    fun init() {
        Log.e(TAG, "init")
        billingClient = BillingClient.newBuilder(builder.activity)
            .setListener { billingResult, purchases ->
                Log.e(TAG, billingResult.responseCode.toString())
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
                    builder.onPurchaseSuccess(purchases.first())
                    for (purchase in purchases) {
                        acceptPurchase(
                            purchase,
                            builder.onPurchaseSuccess,
                            builder.onPurchaseFailed,
                            builder.productsList.firstOrNull()?.type ?: ProductType.INAPP
                        )
                    }
                } else {
                    builder.onPurchaseFailed(billingResult.responseCode)
                }
            }
            .enablePendingPurchases()
            .build()
    }

    private fun acceptPurchase(
        purchase: Purchase,
        onPurchaseSuccess: (purchase: Purchase) -> Unit = {},
        onPurchaseFailed: (responseCode: Int) -> Unit = {},
        productType: String
    ) {
        Log.e(TAG, purchase.toString())
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            when (productType) {
                ProductType.INAPP -> {
                    acceptInAppProduct(purchase, onPurchaseSuccess, onPurchaseFailed)
                }

                ProductType.SUBS -> {
                    acceptSubscription(purchase, onPurchaseSuccess, onPurchaseFailed)
                }
            }
        }
    }

    private fun acceptInAppProduct(
        purchase: Purchase,
        onPurchaseSuccess: (purchase: Purchase) -> Unit,
        onPurchaseFailed: (responseCode: Int) -> Unit
    ) {
//        onPurchaseSuccess(purchase)
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient?.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e(TAG, "acceptInAppProduct onPurchaseSuccess")
                onPurchaseSuccess(purchase)
            } else {
                Log.e(TAG, "acceptInAppProduct onPurchaseFailed")
                onPurchaseFailed(billingResult.responseCode)
            }
        }
    }

    private fun acceptSubscription(
        purchase: Purchase,
        onPurchaseSuccess: (purchase: Purchase) -> Unit,
        onPurchaseFailed: (responseCode: Int) -> Unit
    ) {
        if (!purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
            billingClient?.acknowledgePurchase(acknowledgePurchaseParams.build()) {
                if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.e(TAG, "onPurchaseSuccess")
                    onPurchaseSuccess(purchase)
                } else {
                    Log.e(TAG, "onPurchaseFailed")
                    onPurchaseFailed(it.responseCode)
                }
            }
        } else {
            Log.e(TAG, "isAcknowledged = ${purchase.isAcknowledged}")
            onPurchaseSuccess(purchase)
        }
    }

    fun getProductListFlow(coroutineScope: CoroutineScope): Flow<Result<List<ProductDetails>?>?> {
        return callbackFlow {
            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    Log.e("BillingManager", "onBillingSetupFinished ${billingResult.responseCode}")
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        coroutineScope.launch {
                            val queryProductDetailsParams =
                                QueryProductDetailsParams.newBuilder()
                                    .setProductList(
                                        builder.productsList.map {
                                            QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId(it.id)
                                                .setProductType(it.type)
                                                .build()
                                        }
                                    )
                                    .build()
                            trySend(
                                Result.success(
                                    billingClient?.queryProductDetails(queryProductDetailsParams)?.productDetailsList
                                )
                            ).onFailure {
                                close(it)
                            }
                        }
                    } else {
                        close(Exception("Some error with SKU loading"))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    close(Exception("Some error with Billing Service Connection"))
                }
            })
            awaitClose { billingClient?.endConnection() }
        }
    }

    fun doPurchase(
        activity: Activity,
        productDetails: ProductDetails,
        offerToken: String?
    ) {
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
        offerToken?.let {
            productParams.setOfferToken(offerToken)
        }
        val productDetailsParamsList = listOf(
            productParams.build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient?.launchBillingFlow(activity, billingFlowParams)
    }

    fun getActivePurchasesFlow(
        @ProductType productType: String,
        coroutineScope: CoroutineScope
    ): Flow<Result<MutableList<Purchase>?>?> {
        return callbackFlow {
            if (billingClient?.isReady == true) {
                val currentParams = QueryPurchasesParams.newBuilder()
                    .setProductType(productType)
                coroutineScope.launch {
                    val purchases =
                        billingClient?.queryPurchasesAsync(currentParams.build())?.purchasesList?.toMutableList()
                    trySend(
                        Result.success(purchases)
                    ).onFailure {
                        close(it)
                    }
                }
            } else {
                billingClient?.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            val currentParams = QueryPurchasesParams.newBuilder()
                                .setProductType(productType)
                            coroutineScope.launch {
                                val purchases =
                                    billingClient?.queryPurchasesAsync(currentParams.build())?.purchasesList?.toMutableList()
                                trySend(
                                    Result.success(purchases)
                                ).onFailure {
                                    close(it)
                                }
                            }
                        } else {
                            close(Exception("Some error with paid purchase loading"))
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        close(Exception("Some error with Billing Service Connection"))
                    }
                })
            }
            awaitClose { billingClient?.endConnection() }
        }
    }

    fun getConsumedPurchasesFlow(
        @ProductType productType: String,
        coroutineScope: CoroutineScope
    ): Flow<Result<List<PurchaseHistoryRecord>?>?> {
        return callbackFlow {
            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        val consumedParams = QueryPurchaseHistoryParams.newBuilder()
                            .setProductType(productType)
                        coroutineScope.launch {
                            var consumedPurchases =
                                billingClient?.queryPurchaseHistory(consumedParams.build())?.purchaseHistoryRecordList?.toMutableList()
                            if (!consumedPurchases.isNullOrEmpty()) {
                                trySend(
                                    Result.success(consumedPurchases)
                                ).onFailure {
                                    close(it)
                                }
                            } else {
                                close(Exception("ConsumedPurchases not found"))
                            }
                        }
                    } else {
                        close(Exception("Some error with paid purchase loading"))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    close(Exception("Some error with Billing Service Connection"))
                }
            })
            awaitClose { billingClient?.endConnection() }
        }
    }
}

data class Product(var id: String, @ProductType var type: String)