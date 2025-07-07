package com.respire.mvi.domain.repository

import com.respire.mvi.domain.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getQuizzes() : Flow<Result<List<Quiz>>>
}