package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.model.QuizResult

interface CurrentQuizRepository {
  fun setCurrentQuiz(quiz: Quiz)
  fun getQuizResults(answers: List<Answer<*>>): QuizResult
}