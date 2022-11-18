package com.kartollika.quizzer.domain.model

sealed class Answer<T : PossibleAnswer>(open val questionId: Int) {

  data class SingleChoice(
    val answer: Int,
    override val questionId: Int
  ) : Answer<PossibleAnswer.SingleChoice>(questionId)

  data class Input(
    val value: String,
    override val questionId: Int
  ) :
    Answer<PossibleAnswer.Input>(questionId)
}