package com.respire.mvi.ui.screens.privacyScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.respire.mvi.ui.navigation.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import android.os.Bundle

@HiltViewModel(assistedFactory = PrivacyViewModel.PrivacyViewModelFactory::class)
class PrivacyViewModel @AssistedInject constructor(
    private val app: Application,
    val savedStateHandle: SavedStateHandle,
    @Assisted private val details: String
) : AndroidViewModel(app) {

    var webViewBundle: Bundle? = null

    fun getDetails(): String {
        return if (details.isNotEmpty()) details else savedStateHandle.toRoute<Screen.PrivacyScreen>().details
    }

    @AssistedFactory
    interface PrivacyViewModelFactory {
        fun create(details: String): PrivacyViewModel
    }
}