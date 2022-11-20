package com.kartollika.quizzer.domain.model

interface Answerable

sealed class PossibleAnswer(val name: String) {

  data class Slides(
    val slides: List<Slide> = emptyList()
  ) : PossibleAnswer(SLIDES_TYPE) {

    data class Slide(
      val text: String = "",
      val pictures: List<String> = emptyList()
    )
  }

  data class SingleChoice(
    val options: List<Option> = emptyList(),
    val correctOption: Int = 0
  ) : PossibleAnswer(SINGLE_CHOICE_TYPE), Answerable {
    data class Option(
      val id: Int,
      val value: String = "",
      val linkedQuestionId: Int? = null
    )
  }

  data class Input(
    val hints: List<String> = emptyList(),
    val answer: String = ""
  ) : PossibleAnswer(INPUT_TYPE),
    Answerable {

    init {
      if (hints.size > 2) error("Question can't contain more than 2 hints")
    }
  }

  data class Place(
    val location: Location
  ) : PossibleAnswer(PLACE_TYPE), Answerable

  companion object {
    const val SLIDES_TYPE = "slides"
    const val SINGLE_CHOICE_TYPE = "single_choice"
    const val INPUT_TYPE = "input"
    const val PLACE_TYPE = "place"
  }
}