package com.respire.baseapp.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.respire.baseapp.domain.useCases.SelectedZodiacUseCase
import com.respire.baseapp.domain.useCases.SettingsUseCase
import com.respire.baseapp.ui.navigation.Screen
import com.respire.baseapp.ui.themeScreen.ThemeMode
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
    private val app: Application,
    private val selectedZodiacUseCase: SelectedZodiacUseCase,
    private val settingsUseCase: SettingsUseCase
) : AndroidViewModel(app) {

    private val _screenState = MutableStateFlow<Screen?>(null)
    val screenState: StateFlow<Screen?> = _screenState.asStateFlow()

    private val _themeState = MutableStateFlow(ThemeMode.SYSTEM)
    val themeState: StateFlow<ThemeMode> = _themeState.asStateFlow()

    init {
        getStartDestination()
        getTheme()
    }

    private fun getTheme() {
        settingsUseCase.getSettings()
            .onEach { result ->
                result.onSuccess { data ->
                    _themeState.update { data.themeMode ?: ThemeMode.SYSTEM }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getStartDestination() {
        selectedZodiacUseCase.getSelectedZodiac()
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                result.onSuccess { data ->
                    data?.let {
                        _screenState.update { Screen.ZodiacMain }
                    } ?: run {
                        _screenState.update { Screen.ZodiacList }
                    }
                }
            }
            .onStart { }
            .catch { exception ->
                exception.printStackTrace()
                _screenState.update { Screen.ZodiacList }
            }
            .launchIn(viewModelScope)
    }

    fun updateTheme(themeMode: ThemeMode) {
        _themeState.update { themeMode }
    }
}