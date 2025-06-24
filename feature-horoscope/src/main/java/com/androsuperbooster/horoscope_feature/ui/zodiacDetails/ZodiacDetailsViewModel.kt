package com.androsuperbooster.horoscope_feature.ui.zodiacDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.androsuperbooster.horoscope_feature.ui.navigation.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = ZodiacDetailsViewModel.ZodiacDetailsViewModelFactory::class)
class ZodiacDetailsViewModel @AssistedInject constructor(
    private val app: Application,
    val savedStateHandle: SavedStateHandle,
    @Assisted private val details: String
) : AndroidViewModel(app) {

    fun getDetails(): String {
        return if (details.isNotEmpty()) details else savedStateHandle.toRoute<Screen.ZodiacDetailsScreen>().details
    }

    @AssistedFactory
    interface ZodiacDetailsViewModelFactory {
        fun create(details: String): ZodiacDetailsViewModel
    }
}