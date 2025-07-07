package com.respire.mvi.data.dataSource

import com.respire.mvi.domain.model.Answer
import com.respire.mvi.domain.model.Question
import com.respire.mvi.domain.model.Quiz
import java.util.UUID

class InMemorySource {
    val quizzes: List<Quiz> = listOf(
        Quiz(
            UUID.randomUUID().toString(), "Test Quiz", listOf(
                Question(
                    UUID.randomUUID().toString(),
                    listOf(
                        Answer(UUID.randomUUID().toString(), "Answer 1", true),
                        Answer(UUID.randomUUID().toString(), "Answer 2", false)
                    )
                ),
                Question(
                    UUID.randomUUID().toString(),
                    listOf(
                        Answer(UUID.randomUUID().toString(), "Answer 1", true),
                        Answer(UUID.randomUUID().toString(), "Answer 2", false)
                    )
                )
            )
        ),
        Quiz(
            UUID.randomUUID().toString(), "Test Quiz 2", listOf(
                Question(
                    UUID.randomUUID().toString(),
                    listOf(
                        Answer(UUID.randomUUID().toString(), "Answer 1", true),
                        Answer(UUID.randomUUID().toString(), "Answer 2", false)
                    )
                ),
                Question(
                    UUID.randomUUID().toString(),
                    listOf(
                        Answer(UUID.randomUUID().toString(), "Answer 1", true),
                        Answer(UUID.randomUUID().toString(), "Answer 2", false)
                    )
                )
            )
        )
    )
}