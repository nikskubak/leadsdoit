package com.respire.core_features.presentation.subscriptions

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.orhanobut.hawk.Hawk
import com.respire.core_features.billing.BillingManager
import com.respire.core_features.billing.Product
import com.respire.core_features.data.HawkConstants
import com.respire.core_features.domain.PurchasesRepository
import com.respire.core_features.presentation.BaseUiState
import com.respire.core_features.presentation.ProductsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SubscriptionsViewModel constructor(
    val app: Application,
    var purchasesRepository: PurchasesRepository
) : AndroidViewModel(app) {

    var productList: List<ProductDetails>? = null
    var newBillingManager: BillingManager? = null
    private var _productsUiState = MutableStateFlow<BaseUiState?>(null)
    var productsUiState: StateFlow<BaseUiState?> = _productsUiState

    fun initNewBilling(activity: Activity, onUiSuccess: (purchase : Purchase?) -> Unit = {}) {
        newBillingManager =
            BillingManager.Companion.Builder(activity)
                .productsList(listOf(Product("stretch_subs_month", ProductType.SUBS)))
                .onPurchaseSuccessListener { purchase ->
                    Log.e("onPurchaseSuccessListen", purchase.toString())
                    Hawk.put(HawkConstants.PURCHASES, listOf(purchase))
                    onUiSuccess(purchase)
                }
                .onPurchaseFailedListener {
                    var error = when (it) {
                        BillingResponseCode.FEATURE_NOT_SUPPORTED ->
                            "FEATURE_NOT_SUPPORTED"

                        BillingResponseCode.SERVICE_DISCONNECTED ->
                            "SERVICE_DISCONNECTED"

                        BillingResponseCode.SERVICE_UNAVAILABLE ->
                            "SERVICE_UNAVAILABLE"

                        BillingResponseCode.BILLING_UNAVAILABLE ->
                            "BILLING_UNAVAILABLE"

                        BillingResponseCode.ITEM_UNAVAILABLE -> "ITEM_UNAVAILABLE"
                        BillingResponseCode.DEVELOPER_ERROR -> "DEVELOPER_ERROR"
                        BillingResponseCode.ERROR -> "ERROR"
                        BillingResponseCode.ITEM_ALREADY_OWNED -> {
                            onUiSuccess(Purchase("{}", "signature"))
                            "ITEM_ALREADY_OWNED"
                        }

                        BillingResponseCode.ITEM_NOT_OWNED -> "ITEM_NOT_OWNED"
                        BillingResponseCode.NETWORK_ERROR -> "NETWORK_ERROR"
                        else -> {
                            it.toString()
                        }
                    }
                    Log.e("onPurchaseFailedListene", error)
                }
                .build()
    }


    fun getProductListFlow() {
        newBillingManager?.getProductListFlow(viewModelScope)
            ?.flowOn(Dispatchers.IO)
            ?.onEach { result ->
                Log.e("getProductListFlow", result.toString())
                if (result?.isSuccess == true) {
                    val list = result.getOrDefault(emptyList()) ?: emptyList()
                    productList = list
                    _productsUiState.update {
                        ProductsState(
                            list
                        )
                    }
                } else {
                    result?.exceptionOrNull()?.printStackTrace()
                    _productsUiState.update { BaseUiState.ErrorState(result?.exceptionOrNull()) }
                }
            }
            ?.onStart {
                _productsUiState.update { BaseUiState.LoadingState }
            }
            ?.catch { exception ->
                exception.printStackTrace()
                _productsUiState.update { BaseUiState.ErrorState(exception) }
            }
            ?.launchIn(viewModelScope)
    }

    fun isBaseSubscriptionPlan(sub: ProductDetails.SubscriptionOfferDetails) =
        sub.offerId.isNullOrEmpty()

    fun isFreePricingPhase(offerDetails: ProductDetails.SubscriptionOfferDetails): Boolean {
        var isFree = false
        offerDetails.pricingPhases.pricingPhaseList.forEach {
            if (it.priceAmountMicros == 0L) {
                isFree = true
                return@forEach
            }
        }
        return isFree
    }

    class Factory @Inject constructor(
        var application: Application,
        var purchasesRepository: PurchasesRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SubscriptionsViewModel(
                application,
                purchasesRepository
            ) as T
        }
    }
}