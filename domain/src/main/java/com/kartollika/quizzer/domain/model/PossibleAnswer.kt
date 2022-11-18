package com.kartollika.quizzer.domain.model

interface Answerable

sealed class PossibleAnswer(val name: String) {

  object Empty : PossibleAnswer("empty")

  data class Slides(
    val slides: List<Slide> = emptyList()
  ) : PossibleAnswer("slides") {

    data class Slide(
      val text: String = "",
      val pictures: List<String> = emptyList()
    )
  }

  data class SingleChoice(
    val options: List<Option> = emptyList(),
    val correctOption: Int = 0
  ) : PossibleAnswer("single_choice"), Answerable {
    data class Option(
      val id: Int,
      val value: String = "",
      val linkedQuestionId: Int? = null
    )
  }

  data class Input(
    val hints: List<String> = emptyList(),
    val answer: String = ""
  ) : PossibleAnswer("input"),
    Answerable {

    init {
      if (hints.size > 2) error("Question can't contain more than 2 hints")
    }
  }

  data class Place(
    val location: Location
  ) : PossibleAnswer("place"), Answerable
}