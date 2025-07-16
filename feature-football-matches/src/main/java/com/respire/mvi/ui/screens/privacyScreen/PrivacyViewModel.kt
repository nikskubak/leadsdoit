package com.respire.mvi.ui.screens.privacyScreen

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.respire.mvi.ui.navigation.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = PrivacyViewModel.PrivacyViewModelFactory::class)
class PrivacyViewModel @AssistedInject constructor(
    val savedStateHandle: SavedStateHandle,
    @Assisted private val details: String
) : ViewModel() {

    var webViewBundle: Bundle? = null

    fun getDetails(): String {
        return if (details.isNotEmpty()) details else savedStateHandle.toRoute<Screen.PrivacyScreen>().details
    }

    @AssistedFactory
    interface PrivacyViewModelFactory {
        fun create(details: String): PrivacyViewModel
    }
}