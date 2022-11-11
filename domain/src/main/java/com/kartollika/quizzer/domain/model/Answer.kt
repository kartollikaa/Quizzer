package com.kartollika.quizzer.domain.model

sealed class Answer<T : PossibleAnswer> {
  data class SingleChoice(val answer: String) : Answer<PossibleAnswer.SingleChoice>()
  data class MultipleChoice(val answers: Set<String>) : Answer<PossibleAnswer.MultipleChoice>()
  data class Input(val value: String) : Answer<PossibleAnswer.Input>()
}