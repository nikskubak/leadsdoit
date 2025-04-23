package com.respire.baseapp.ui.zodiacMain.settings

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.respire.baseapp.domain.model.SettingsEntity
import com.respire.baseapp.domain.model.ZodiacEntity
import com.respire.baseapp.domain.useCases.SelectedZodiacUseCase
import com.respire.baseapp.domain.useCases.SettingsUseCase
import com.respire.baseapp.ui.base.BaseUiState
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
class SettingsViewModel @Inject constructor(
    private val app: Application,
    private val selectedZodiacUseCase: SelectedZodiacUseCase,
    private val settingsUseCase: SettingsUseCase
) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.LoadingState)
    val uiState: StateFlow<BaseUiState> = _uiState.asStateFlow()


    init {
        getSettings()
    }

    private fun getSettings() {
        selectedZodiacUseCase.getSelectedZodiac()
            .zip(settingsUseCase.getSettings()) { zodiacEntityResult: Result<ZodiacEntity?>,
                                                  settingsEntityResult: Result<SettingsEntity> ->
                zodiacEntityResult.onSuccess { zodiac ->
                    settingsEntityResult.onSuccess { settings ->
                        zodiac?.let {
                            _uiState.update {
                                BaseUiState.ContentState(
                                    SettingsUi(
                                        zodiac,
                                        settings.themeMode ?: ThemeMode.SYSTEM,
                                        settings.isNotificationEnabled ?: true
                                    )
                                )
                            }
                        }
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .onStart { _uiState.update { BaseUiState.LoadingState } }
            .catch { exception ->
                exception.printStackTrace()
                _uiState.update { BaseUiState.ErrorState(exception) }
            }
            .launchIn(viewModelScope)
    }

    fun saveTheme(theme: ThemeMode) {
        settingsUseCase.saveSettings(SettingsEntity(themeMode = theme))
            .launchIn(viewModelScope)
    }

    fun saveNotification(checked: Boolean) {
        settingsUseCase.saveSettings(SettingsEntity(isNotificationEnabled = checked))
            .onEach { getSettings() }
            .launchIn(viewModelScope)
    }
}