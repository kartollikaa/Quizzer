package com.kartollika.quizzer.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kartollika.quizzer.data.adapters.PossibleAnswerDeserializer
import com.kartollika.quizzer.data.adapters.PossibleAnswerSerializer
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.Question
import com.kartollika.quizzer.domain.model.Quiz
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class QuestConverterTest {

  private lateinit var gson: Gson

  @Before fun setup() {
    gson = GsonBuilder()
      .registerTypeAdapter(PossibleAnswer::class.java, PossibleAnswerSerializer())
      .registerTypeAdapter(PossibleAnswer::class.java, PossibleAnswerDeserializer())
      .create()
  }

  @Test
  fun Quest_One_Step_To_Gson() {
    val quest = Quiz(
      title = "My Quiz",
      questions = listOf(
        Question(
          id = 1,
          questionText = "Say my name",
          answer = SingleChoice(
            listOf(
              "Ukka",
              "Heisenberg",
              "Kirill"
            )
          )
        ),
        Question(
          id = 2,
          questionText = "Type my name",
          answer = Input(
            hints = listOf(
              "I am the one who knocks",
              "I killed Gustavo Fling",
            )
          )
        )
      )
    )

    val questJson = gson.toJson(quest)
    println(questJson)

    val expected = QUEST
    assertEquals(expected, questJson)
  }

  @Test
  fun Quest_Json_To_Model() {
    val questJson = QUEST

    val quest = gson.fromJson(questJson, Quiz::class.java)

    val expected = Quiz(
      title = "My Quiz",
      questions = listOf(
        Question(
          id = 1,
          questionText = "Say my name",
          answer = SingleChoice(
            listOf(
              "Ukka",
              "Heisenberg",
              "Kirill"
            )
          )
        )
      )
    )

    assertEquals(expected, quest)
  }

  companion object {
    private val QUEST = """
      {"title":"My Quiz","questions":[{"id":1,"questionText":"Say my name","answer":{"options":["Ukka","Heisenberg","Kirill"],"name":"single_choice"}}]}
      """
      .trimIndent()
  }
}