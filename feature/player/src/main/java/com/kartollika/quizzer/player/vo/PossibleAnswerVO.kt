package com.kartollika.quizzer.player.vo

import android.graphics.Bitmap

sealed class PossibleAnswerVO {
  data class Slides(val slides: List<Slide>): PossibleAnswerVO() {
    data class Slide(
      val text: String?,
      val pictures: List<Bitmap>?
    )
  }
  data class SingleChoice(val options: List<String>, val correctOption: String) : PossibleAnswerVO()
  data class MultipleChoice(val options: List<String>) : PossibleAnswerVO()
  data class Input(val hints: List<String>, val answer: String): PossibleAnswerVO()
}