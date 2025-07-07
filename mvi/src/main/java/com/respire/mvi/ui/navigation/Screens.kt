package com.respire.mvi.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(var titleRes: Int? = null, var iconRes: Int? = null) {
    @Serializable
    data object QuizList : Screen()
}

