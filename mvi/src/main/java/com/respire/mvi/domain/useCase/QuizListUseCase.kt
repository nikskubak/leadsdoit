package com.respire.mvi.domain.useCase

import com.respire.mvi.domain.model.Quiz
import com.respire.mvi.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizListUseCase @Inject constructor(val quizRepository: QuizRepository){
    operator fun invoke(): Flow<Result<List<Quiz>>> {
        return quizRepository.getQuizzes()
    }
}