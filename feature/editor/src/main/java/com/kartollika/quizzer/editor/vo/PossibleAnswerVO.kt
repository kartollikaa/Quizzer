package com.kartollika.quizzer.editor.vo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kartollika.quizzer.editor.bitmap.BitmapDecoder.DecodeResult

sealed class PossibleAnswerVO {

  object Empty : PossibleAnswerVO()

  class Slides(initialSlide: Slide = Slide()) : PossibleAnswerVO() {

    var slides: List<Slide> by mutableStateOf(listOf(initialSlide))

    class Slide {
      var text by mutableStateOf("")
      var pictures: List<DecodeResult> by mutableStateOf(emptyList())
    }
  }

  class SingleChoice(initialOption: OptionVO) : PossibleAnswerVO() {

    var options: List<OptionVO> by mutableStateOf(listOf(initialOption))
    var correctOption by mutableStateOf(0)

    data class OptionVO(val id: Int) {

      var value: String by mutableStateOf("")
      var linkedQuestionId: Int? by mutableStateOf(null)

      fun isNotEmpty() = value.isNotEmpty()
    }
  }

  class Input : PossibleAnswerVO() {
    var hints: List<String> by mutableStateOf(emptyList())
    var answer by mutableStateOf("")
  }
}