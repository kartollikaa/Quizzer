package com.kartollika.quizzer.domain.model

data class Question(
  val id: Int,
  val questionText: String = "",
  val answer: PossibleAnswer,
)
