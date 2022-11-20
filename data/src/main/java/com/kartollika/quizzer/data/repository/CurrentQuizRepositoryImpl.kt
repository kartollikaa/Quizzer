package com.kartollika.quizzer.data.repository

import com.kartollika.quizzer.data.core.toInt
import com.kartollika.quizzer.domain.datasource.QuizFileDataSource
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.Answer.Place
import com.kartollika.quizzer.domain.model.Answer.Place.BeenHere.BEEN
import com.kartollika.quizzer.domain.model.Answerable
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.model.QuizResult
import com.kartollika.quizzer.domain.repository.CurrentQuizRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrentQuizRepositoryImpl @Inject constructor(
  private val quizFileDataSource: QuizFileDataSource
) : CurrentQuizRepository {

  private var quiz: Quiz? = null

  override fun openQuiz(file: String) = flow {
    val quiz = quizFileDataSource.readFile(file)
    this@CurrentQuizRepositoryImpl.quiz = quiz
    emit(quiz)
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  override fun getQuizResults(answers: List<Answer<*>>): QuizResult {
    val quiz = quiz
    quiz ?: error("No quiz is in progress")
    var correctAnswers = 0
    val possibleAnswers = quiz.questions.filter { it.answer is Answerable }
    val totalQuestions = answers.size

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
          correctAnswers += (answer.optionId == correctAnswer.correctOption).toInt()
        }
        is PossibleAnswer.Place -> {
          answer as Place
          correctAnswers += (answer.beenHere == BEEN).toInt()
        }

        else -> {}
      }
    }
    return QuizResult(correctAnswers, totalQuestions)
  }
}