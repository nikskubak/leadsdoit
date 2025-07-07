package com.respire.mvi.ui.quizList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.respire.mvi.domain.model.Quiz
import com.respire.mvi.domain.repository.QuizRepository
import com.respire.mvi.domain.useCase.QuizListUseCase
import com.respire.mvi.ui.quizList.state.QuizListEffect
import com.respire.mvi.ui.quizList.state.QuizListEvent
import com.respire.mvi.ui.quizList.state.QuizListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizListViewModel @Inject constructor(val quizListUseCase: QuizListUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizListState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<QuizListEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    private fun getQuizzes() {
        quizListUseCase()
            .onStart {
                _uiState.value = _uiState.value.copy(isLoading = true, quizzes = emptyList())
            }
            .onEach {
                it.onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, quizzes = it)
                }
                it.onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(quizListEvent: QuizListEvent) {
        when (quizListEvent) {
            is QuizListEvent.ClickOnItemEvent -> {
                viewModelScope.launch {
                    _uiEffect.emit(QuizListEffect.SelectItemEvent(quizListEvent.quizId))
                }
            }

            is QuizListEvent.LoadListEvent -> {
                getQuizzes()
            }
        }
    }
}