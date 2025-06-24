package com.androsuperbooster.horoscope_feature.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.androsuperbooster.horoscope_feature.domain.useCases.GetCurrenciesUseCase
import com.androsuperbooster.horoscope_feature.ui.base.BaseUiState
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val app: Application,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : AndroidViewModel(app) {

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.LoadingState)
    val uiState: StateFlow<BaseUiState> = _uiState.asStateFlow()


    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        getCurrenciesUseCase()
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                result.onSuccess { data ->
                    _uiState.update { BaseUiState.ContentState(MainUi(data, simpleDateFormat.format(Date()))) }
                }
                result.onFailure { error ->
                    _uiState.update { BaseUiState.ErrorState(error) }
                }
            }
            .onStart { _uiState.update { BaseUiState.LoadingState } }
            .catch { exception ->
                exception.printStackTrace()
                _uiState.update { BaseUiState.ErrorState(exception) }
            }
            .launchIn(viewModelScope)
    }
}