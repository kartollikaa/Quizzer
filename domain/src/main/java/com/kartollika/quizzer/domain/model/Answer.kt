package com.kartollika.quizzer.domain.model

sealed class Answer<T : PossibleAnswer>(open val questionId: Int) {

  data class SingleChoice(
    val answer: String,
    override val questionId: Int
  ) : Answer<PossibleAnswer.SingleChoice>(questionId)

  data class MultipleChoice(
    val answers: Set<String>,
    override val questionId: Int
  ) :
    Answer<PossibleAnswer.MultipleChoice>(questionId)

  data class Input(
    val value: String,
    override val questionId: Int
  ) :
    Answer<PossibleAnswer.Input>(questionId)
}