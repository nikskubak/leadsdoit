package com.respire.mvi.ui.settings

import androidx.lifecycle.ViewModel
import com.respire.mvi.R
import com.respire.mvi.ui.settings.state.SettingsAction
import com.respire.mvi.ui.settings.state.SettingsEffect
import com.respire.mvi.ui.settings.state.SettingsEvent
import com.respire.mvi.ui.settings.state.SettingsItem
import com.respire.mvi.ui.settings.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val settingsItems = listOf(
            SettingsItem(
                title = "Themes",
                icon = R.drawable.ic_notifications,
                action = SettingsAction.THEMES
            ),
            SettingsItem(
                title = "Privacy Policy",
                icon = R.drawable.ic_notifications,
                action = SettingsAction.PRIVACY_POLICY
            )
        )

        _state.value = _state.value.copy(settingsItems = settingsItems)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LoadSettings -> {
                loadSettings()
            }

            is SettingsEvent.OnSettingsItemClicked -> {
                handleSettingsAction(event.action)
            }
        }
    }

    private fun handleSettingsAction(action: SettingsAction) {
        when (action) {
            SettingsAction.THEMES -> {
                CoroutineScope(Dispatchers.Main).launch {
                    _effect.emit(SettingsEffect.NavigateToThemes)
                }
            }

            SettingsAction.PRIVACY_POLICY -> {
                CoroutineScope(Dispatchers.Main).launch {
                    _effect.emit(SettingsEffect.NavigateToPrivacyPolicy)
                }
            }
        }
    }
} 