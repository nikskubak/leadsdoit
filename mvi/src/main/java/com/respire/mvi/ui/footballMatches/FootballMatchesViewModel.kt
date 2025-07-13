package com.respire.mvi.ui.footballMatches

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.respire.mvi.domain.model.FixtureEntity
import com.respire.mvi.domain.useCase.FootballMatchesUseCase
import com.respire.mvi.ui.footballMatches.state.FilterType
import com.respire.mvi.ui.footballMatches.state.FootballMatchesEffect
import com.respire.mvi.ui.footballMatches.state.FootballMatchesEvent
import com.respire.mvi.ui.footballMatches.state.FootballMatchesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FootballMatchesViewModel @Inject constructor(val matchesUseCase: FootballMatchesUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(FootballMatchesState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FootballMatchesEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    // Keep original matches list for filtering
    private var originalMatches: List<FixtureEntity> = emptyList()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    init {
        getMatches()
    }

    private fun getMatches(refreshing: Boolean = false) {
        matchesUseCase(dateFormat.format(Date()))
            .flowOn(Dispatchers.IO)
            .onStart {
                if (!refreshing)
                    _uiState.value = _uiState.value.copy(isLoading = true)
                else
                    _uiState.value = _uiState.value.copy(isRefreshing = true)
            }
            .onEach {
                it.onSuccess { matches ->
                    originalMatches = matches // Store original matches
                    val filteredMatches = getFilteredMatches(matches, _uiState.value.selectedFilter)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        matches = filteredMatches
                    )
                }
                it.onFailure {
                    _uiState.value = _uiState.value.copy(
                        error = it.message, isLoading = false,
                        isRefreshing = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: FootballMatchesEvent) {
        when (event) {
            is FootballMatchesEvent.ClickOnItemEvent -> {
                viewModelScope.launch {
                    _uiEffect.emit(FootballMatchesEffect.SelectItemEvent(event.matchId))
                }
            }

            is FootballMatchesEvent.FilterChangedEvent -> {
                val filteredMatches = getFilteredMatches(originalMatches, event.filterType)
                _uiState.value = _uiState.value.copy(
                    selectedFilter = event.filterType,
                    matches = filteredMatches
                )
            }

            is FootballMatchesEvent.LoadListEvent -> {
                getMatches()
            }

            is FootballMatchesEvent.OnRefreshedListEvent -> {
                getMatches(true)
            }
        }
    }

    private fun getFilteredMatches(
        matches: List<FixtureEntity>,
        filterType: FilterType
    ): List<FixtureEntity> {
        return when (filterType) {
            FilterType.ALL -> matches
            FilterType.LIVE -> matches.filter { it.fixture.status.isLive }
            FilterType.ENDED -> matches.filter { it.fixture.status.hasStarted && !it.fixture.status.isLive }
            FilterType.NOT_STARTED -> matches.filter { !it.fixture.status.hasStarted }
        }
    }
}