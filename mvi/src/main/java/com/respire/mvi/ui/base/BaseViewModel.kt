package com.respire.mvi.ui.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.hawk.Hawk
import com.respire.mvi.domain.useCase.SettingsUseCase
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    app: Application,
    private val settingsUseCase: SettingsUseCase
) : AndroidViewModel(app) {

    private val _screenState = MutableStateFlow<Screen?>(null)
    val screenState: StateFlow<Screen?> = _screenState.asStateFlow()

    private val _themeState = MutableStateFlow(ThemeMode.SYSTEM)
    val themeState: StateFlow<ThemeMode> = _themeState.asStateFlow()

    init {
        getTheme()
    }

    private fun getTheme() {
        settingsUseCase.getSettings()
            .onEach { result ->
                result.onSuccess { data ->
                    _themeState.update { ThemeMode.entries[data.themeMode ?: 2] }
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateTheme(themeMode: ThemeMode) {
        _themeState.update { themeMode }
    }
}