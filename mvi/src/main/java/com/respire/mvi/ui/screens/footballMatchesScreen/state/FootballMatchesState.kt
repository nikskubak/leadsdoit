package com.respire.mvi.ui.screens.footballMatchesScreen.state

import com.respire.mvi.R
import com.respire.mvi.domain.model.FixtureEntity
import java.util.Date

enum class FilterType(val nameRes: Int) {
    ALL(R.string.all),
    LIVE(R.string.live),
    ENDED(R.string.finished),
    NOT_STARTED(R.string.next)
}

data class FootballMatchesState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val matches: List<FixtureEntity> = emptyList(),
    val selectedFilter: FilterType = FilterType.ALL,
    val selectedDate: Date = Date()
)

sealed class FootballMatchesEffect {
    data class SelectItemEvent(val matchId: Int) : FootballMatchesEffect()
    data class ErrorEffect(val message: String) : FootballMatchesEffect()
}

sealed class FootballMatchesEvent{
    data class ClickOnItemEvent(val matchId: Int) : FootballMatchesEvent()
    data class FilterChangedEvent(val filterType: FilterType) : FootballMatchesEvent()
    object OnRefreshedListEvent: FootballMatchesEvent()
    object LoadListEvent : FootballMatchesEvent()
    data class OnDateSelected(val date: Date) : FootballMatchesEvent()
}
