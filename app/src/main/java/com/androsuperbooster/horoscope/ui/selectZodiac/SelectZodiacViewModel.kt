package com.androsuperbooster.horoscope.ui.selectZodiac

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.androsuperbooster.horoscope.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope.domain.useCases.GetZodiacSignsUseCase
import com.androsuperbooster.horoscope.domain.useCases.SelectedZodiacUseCase
import com.androsuperbooster.horoscope.ui.base.BaseUiState
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
class SelectZodiacViewModel @Inject constructor(
    private val app: Application,
    private val getZodiacSignsUseCase: GetZodiacSignsUseCase,
    private val selectedZodiacUseCase: SelectedZodiacUseCase
) : AndroidViewModel(app) {

    lateinit var onZodiacSelected: (ZodiacEntity) -> Unit
    private val _uiState = MutableStateFlow<BaseUiState>(BaseUiState.LoadingState)
    val uiState: StateFlow<BaseUiState> = _uiState.asStateFlow()


    init {
        getZodiacSigns()
    }

    private fun getZodiacSigns() {
        getZodiacSignsUseCase()
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                result.onSuccess { data ->
                    _uiState.update { BaseUiState.ContentState(SelectZodiacSignsUi(data)) }
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

    fun selectSign(zodiacEntity: ZodiacEntity) {
        selectedZodiacUseCase.saveSelectedZodiac(zodiacEntity)
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                result.onSuccess { data ->
                    onZodiacSelected(zodiacEntity)
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