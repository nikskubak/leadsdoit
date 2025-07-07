package com.respire.mvi.data.repository

import com.respire.mvi.data.dataSource.InMemorySource
import com.respire.mvi.domain.model.Quiz
import com.respire.mvi.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(val inMemorySource: InMemorySource) : QuizRepository {
    override fun getQuizzes(): Flow<Result<List<Quiz>>> {
        return flow {
            emit(Result.success(inMemorySource.quizzes))
        }
    }
}