package com.respire.mvi.ui.footballMatches.state

import com.respire.mvi.domain.model.FixtureEntity

enum class FilterType(val displayName: String) {
    ALL("All"),
    ENDED("Ended"),
    LIVE("Live"),
    NOT_STARTED("Not Started")
}

data class FootballMatchesState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val matches: List<FixtureEntity> = emptyList(),
    val error: String? = null,
    val selectedFilter: FilterType = FilterType.ALL
)

sealed class FootballMatchesEffect {
    data class SelectItemEvent(val matchId: Int) : FootballMatchesEffect()
}

sealed class FootballMatchesEvent{
    data class ClickOnItemEvent(val matchId: Int) : FootballMatchesEvent()
    data class FilterChangedEvent(val filterType: FilterType) : FootballMatchesEvent()
    object OnRefreshedListEvent: FootballMatchesEvent()
    object LoadListEvent : FootballMatchesEvent()
}
