package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface CurrentQuizRepository {
  fun openQuiz(file: String): Flow<Quiz>
  fun getQuizResults(answers: List<Answer<*>>): QuizResult
}