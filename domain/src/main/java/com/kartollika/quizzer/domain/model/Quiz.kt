package com.kartollika.quizzer.domain.model

data class Quiz(
  val title: String,
  val questions: List<Question> = emptyList()
)
