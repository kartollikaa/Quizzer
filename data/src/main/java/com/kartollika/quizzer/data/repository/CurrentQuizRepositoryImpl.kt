package com.kartollika.quizzer.data.repository

import com.kartollika.quizzer.data.core.toInt
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.Answerable
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.MultipleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.model.QuizResult
import com.kartollika.quizzer.domain.repository.CurrentQuizRepository
import javax.inject.Inject

class CurrentQuizRepositoryImpl @Inject constructor() : CurrentQuizRepository {

  private var quiz: Quiz? = null

  override fun setCurrentQuiz(quiz: Quiz) {
    this.quiz = quiz
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  override fun getQuizResults(answers: List<Answer<*>>): QuizResult {
    val quiz = quiz
    quiz ?: error("No quiz is in progress")
    var correctAnswers = 0
    var totalQuestions = 0
    val possibleAnswers = quiz.questions.filter { it.answer is Answerable }
    totalQuestions = possibleAnswers.size

    answers.forEachIndexed { index, answer: Answer<*> ->
      val possibleAnswer = possibleAnswers.find { it.id == answer.questionId }
      possibleAnswer ?: error("No such question ${answer.questionId}")

      when (val correctAnswer = possibleAnswer.answer) {
        is Input -> {
          answer as Answer.Input
          correctAnswers += (answer.value.trim() == correctAnswer.answer.trim()).toInt()
        }
        is SingleChoice -> {
          answer as Answer.SingleChoice
          correctAnswers += (answer.answer == correctAnswer.correctOption).toInt()
        }
        is MultipleChoice -> {
          answer as Answer.MultipleChoice
          correctAnswers += (answer.answers == correctAnswer.options).toInt()
        }
        else -> {}
      }
    }
    return QuizResult(correctAnswers, totalQuestions)
  }
}