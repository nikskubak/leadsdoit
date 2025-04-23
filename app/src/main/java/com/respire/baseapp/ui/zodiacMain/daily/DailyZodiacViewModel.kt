package com.respire.baseapp.ui.zodiacMain.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.respire.baseapp.domain.useCases.SelectedZodiacUseCase
import com.respire.baseapp.ui.base.BaseUiState
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
class DailyZodiacViewModel @Inject constructor(
    private val app: Application,
    private val selectedZodiacUseCase: SelectedZodiacUseCase
) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.LoadingState)
    val uiState: StateFlow<BaseUiState> = _uiState.asStateFlow()


    init {
        getSelectedZodiac()
    }

    private fun getSelectedZodiac() {
        selectedZodiacUseCase.getSelectedZodiac()
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                result.onSuccess { data ->
                    data?.let {
                        _uiState.update { BaseUiState.ContentState(DailyZodiacSignsUi(data)) }
                    } ?: run{
                        _uiState.update { BaseUiState.ErrorState(Exception("Empty zodiac")) }
                    }
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