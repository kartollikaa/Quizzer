package com.kartollika.quizzer.domain.model

sealed class Answer<T : PossibleAnswer>(open val questionId: Int) {

  data class SingleChoice(
    val optionId: Int,
    override val questionId: Int
  ) : Answer<PossibleAnswer.SingleChoice>(questionId)

  data class Input(
    val value: String,
    override val questionId: Int
  ) : Answer<PossibleAnswer.Input>(questionId)

  data class Place(
    val beenHere: Boolean,
    val location: Location
  )
}