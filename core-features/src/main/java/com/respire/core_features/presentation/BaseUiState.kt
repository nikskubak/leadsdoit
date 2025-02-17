package com.respire.core_features.presentation

import com.android.billingclient.api.ProductDetails

sealed class BaseUiState {
    object LoadingState : BaseUiState()
    class ErrorState(var exception: Throwable?) : BaseUiState()
    open class ContentState<T>(var content: T? = null) : BaseUiState()
}
class ProductsState(var list : List<ProductDetails>) : BaseUiState.ContentState<List<ProductDetails>>(list)


