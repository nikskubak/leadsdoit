package com.androsuperbooster.horoscope.ui.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.androsuperbooster.horoscope.data.HawkKeys
import com.androsuperbooster.horoscope.domain.useCases.InstallReferrerUseCase
import com.androsuperbooster.horoscope.domain.useCases.SelectedZodiacUseCase
import com.androsuperbooster.horoscope.domain.useCases.SettingsUseCase
import com.androsuperbooster.horoscope.ui.navigation.Screen
import com.androsuperbooster.horoscope.ui.themeScreen.ThemeMode
import com.orhanobut.hawk.Hawk
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
    private val settingsUseCase: SettingsUseCase,
    private val installReferrerUseCase: InstallReferrerUseCase
) : AndroidViewModel(app) {

    private val _screenState = MutableStateFlow<Screen?>(null)
    val screenState: StateFlow<Screen?> = _screenState.asStateFlow()

    private val _themeState = MutableStateFlow(ThemeMode.SYSTEM)
    val themeState: StateFlow<ThemeMode> = _themeState.asStateFlow()

    init {
        getStartDestination()
        getTheme()
        getInstallReferrer()
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

    fun getInstallReferrer() {
        installReferrerUseCase()
            .flowOn(Dispatchers.IO)
            .catch { exception ->
                exception.printStackTrace()
            }
            .launchIn(viewModelScope)
    }
}