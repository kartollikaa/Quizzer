package com.kartollika.quizzer.domain.model

import com.kartollika.quizzer.domain.model.Answer.Place.BeenHere.NOT_STATED

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
    val beenHere: BeenHere = NOT_STATED,
    val isConfirmEnabled: Boolean = false,
    override val questionId: Int = -1
  ) : Answer<PossibleAnswer.Place>(questionId) {
    enum class BeenHere {
      BEEN,
      REJECTED,
      NOT_STATED
    }
  }
}