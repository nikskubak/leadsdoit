package com.respire.baseapp.ui.zodiacDetails

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.respire.baseapp.BuildConfig
import com.respire.baseapp.domain.model.SettingsEntity
import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.domain.useCases.SelectedZodiacUseCase
import com.respire.baseapp.domain.useCases.SettingsUseCase
import com.respire.baseapp.ui.base.BaseUiState
import com.respire.baseapp.ui.navigation.Screen
import com.respire.baseapp.ui.themeScreen.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@HiltViewModel
class ZodiacDetailsViewModel @Inject constructor(
    private val app: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    fun getDetails() : String{
        return savedStateHandle.toRoute<Screen.ZodiacDetailsScreen>().details
    }
}