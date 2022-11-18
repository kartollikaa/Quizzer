package com.kartollika.quizzer.player.vo

import android.graphics.Bitmap

sealed class PossibleAnswerVO {
  data class Slides(val slides: List<Slide>) : PossibleAnswerVO() {
    data class Slide(
      val text: String?,
      val pictures: List<Bitmap>?
    )
  }

  data class SingleChoice(
    val options: List<OptionVO>,
    val correctOption: Int,
  ) :
    PossibleAnswerVO() {
    data class OptionVO(
      val id: Int,
      val value: String,
      val linkedQuestionId: Int? = null
    )
  }

  data class Input(val hints: List<String>, val answer: String) : PossibleAnswerVO()
}