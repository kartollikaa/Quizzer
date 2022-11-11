package com.kartollika.quizzer.domain.model

sealed class PossibleAnswer(val name: String) {
  data class SingleChoice(val options: List<String>, val correctOption: String) : PossibleAnswer("single_choice")
  data class MultipleChoice(val options: List<String>) : PossibleAnswer("multiple_choice")
  data class Input(val hints: List<String>): PossibleAnswer("input")
}