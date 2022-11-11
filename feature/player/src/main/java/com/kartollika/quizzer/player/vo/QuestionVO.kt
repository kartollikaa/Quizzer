package com.kartollika.quizzer.player.vo

data class QuestionVO(
  val id: Int,
  val questionText: String,
  val answer: PossibleAnswerVO,
)