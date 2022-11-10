package com.kartollika.quizzer.domain.model

sealed class StepType(val name: String)

data class Slides(
  val pages: List<Page>
): StepType("slides") {

  data class Page(
    val text: String? = null,
    val pictures: List<String>? = null
  )
}

data class Input(
  val description: String,
  val answer: String,
  val hints: List<String>
): StepType("input")

data class Choice(
  val description: String,
  val options: List<Option>,
  val correctOptions: List<Option>
): StepType("choice") {
  data class Option(val text: String)
}
