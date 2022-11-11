package com.kartollika.quizzer.domain.model

interface Answerable

sealed class PossibleAnswer(val name: String) {
  data class Slides(val slides: List<Slide>): PossibleAnswer("slides") {
    data class Slide(
      val text: String?,
      val pictures: List<String>?
    )
  }
  data class SingleChoice(val options: List<String>, val correctOption: String) : PossibleAnswer("single_choice"), Answerable
  data class MultipleChoice(val options: List<String>) : PossibleAnswer("multiple_choice"), Answerable
  data class Input(val hints: List<String>, val answer: String): PossibleAnswer("input"), Answerable
}