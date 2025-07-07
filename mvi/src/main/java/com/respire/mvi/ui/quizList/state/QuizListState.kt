package com.respire.mvi.ui.quizList.state

import com.respire.mvi.domain.model.Quiz

data class QuizListState(
    val isLoading: Boolean = false,
    val quizzes: List<Quiz> = emptyList(),
    val error: String? = null
)

sealed class QuizListEffect {
    data class SelectItemEvent(val quizId: String) : QuizListEffect()
}

sealed class QuizListEvent{
    data class ClickOnItemEvent(val quizId: String) : QuizListEvent()
    object LoadListEvent : QuizListEvent()
}