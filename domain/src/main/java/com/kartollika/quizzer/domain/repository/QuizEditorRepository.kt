package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quiz
import kotlinx.coroutines.flow.Flow
import java.io.File

interface QuizEditorRepository {
  fun getNextQuestionId(): Int
  fun getNextId(): Int
  fun generateQuiz(quiz: Quiz): Flow<File>
}