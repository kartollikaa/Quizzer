package com.kartollika.quizzer.player

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides
import com.kartollika.quizzer.domain.model.Question
import com.kartollika.quizzer.domain.model.QuizResult

@Stable
data class QuestionState(
  val question: Question,
  val questionIndex: Int,
  val totalQuestionsCount: Int,
  val showPrevious: Boolean,
  val showDone: Boolean
) {
  var enableCheck by mutableStateOf(true)
  var enableNext by mutableStateOf(false)
  var answer by mutableStateOf<Answer<*>?>(null)
  var checked by mutableStateOf(question.answer is Slides)
}

sealed class QuizState {

  object Loading: QuizState()
  data class Error(val cause: String): QuizState()

  data class Questions(
    val quizTitle: String,
    val questionsState: List<QuestionState>
  ): QuizState() {
    var currentQuestionIndex by mutableStateOf(0)
  }

  data class Result(
    val quizTitle: String,
    val quizResult: QuizResult
  ): QuizState()
}
