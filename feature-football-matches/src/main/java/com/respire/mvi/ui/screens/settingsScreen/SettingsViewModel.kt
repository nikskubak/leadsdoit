package com.respire.mvi.ui.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.respire.mvi.domain.useCase.SettingsUseCase
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.screens.settingsScreen.state.SettingsEffect
import com.respire.mvi.ui.screens.settingsScreen.state.SettingsEvent
import com.respire.mvi.ui.screens.settingsScreen.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val settingsUseCase: SettingsUseCase) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        settingsUseCase.getSettings()
            .flowOn(Dispatchers.IO)
            .onEach {
                it.onSuccess {
                    _state.value = _state.value.copy(settingsEntity = it)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LoadSettings -> {
                loadSettings()
            }

            is SettingsEvent.OnPrivacyClicked -> {
                CoroutineScope(Dispatchers.Main).launch {
                    _effect.emit(SettingsEffect.NavigateToPrivacyPolicy(_state.value.settingsEntity?.privacyPolicyUrl))
                }
            }

            is SettingsEvent.OnThemeChanged -> {
                CoroutineScope(Dispatchers.Main).launch {
                    _effect.emit(SettingsEffect.NavigateToThemes)
                }
            }

            is SettingsEvent.OnNotificationChanged -> {
                val settingsEntity =
                    _state.value.settingsEntity?.copy(isNotificationEnabled = event.isEnable)
                _state.value = _state.value.copy(settingsEntity = settingsEntity)
                settingsUseCase.saveSettings(settingsEntity)
                    .launchIn(viewModelScope)
            }
        }
    }

    fun saveTheme(theme: ThemeMode) {
        settingsUseCase.saveSettings(_state.value.settingsEntity?.copy(themeMode = theme.ordinal))
            .launchIn(viewModelScope)
    }
} 