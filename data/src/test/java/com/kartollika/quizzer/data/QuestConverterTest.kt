package com.kartollika.quizzer.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kartollika.quizzer.data.adapters.StepTypeDeserializer
import com.kartollika.quizzer.data.adapters.StepTypeSerializer
import com.kartollika.quizzer.domain.model.Choice
import com.kartollika.quizzer.domain.model.Choice.Option
import com.kartollika.quizzer.domain.model.Quest
import com.kartollika.quizzer.domain.model.Step
import com.kartollika.quizzer.domain.model.StepType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class QuestConverterTest {

  private lateinit var gson: Gson

  @Before fun setup() {
    gson = GsonBuilder()
      .registerTypeAdapter(StepType::class.java, StepTypeSerializer())
      .registerTypeAdapter(StepType::class.java, StepTypeDeserializer())
      .create()
  }

  @Test
  fun Quest_One_Step_To_Gson() {
    val option1 = Option("1")
    val option2 = Option("2")
    val quest = Quest(
      listOf(
        Step(
          Choice(
            "123",
            listOf(option1, option2),
            listOf(option1)
          )
        )
      )
    )
    val questJson = gson.toJson(quest)

    val expected = """
      {"steps":[{"type":{"description":"123","options":[{"text":"1"},{"text":"2"}],"correctOptions":[{"text":"1"}],"name":"choice"}}]}
      """.trimIndent()
    assertEquals(expected, questJson)
  }

  @Test
  fun Quest_Json_To_Model() {
    val questJson = """
      {"steps":[{"type":{"description":"123","options":[{"text":"1"},{"text":"2"}],"correctOptions":[{"text":"1"}],"name":"choice"}}]}
      """.trimIndent()

    val quest = gson.fromJson(questJson, Quest::class.java)

    val option1 = Option("1")
    val option2 = Option("2")
    val expected = Quest(
      listOf(
        Step(
          Choice(
            "123",
            listOf(option1, option2),
            listOf(option1)
          )
        )
      )
    )

    assertEquals(expected, quest)
  }
}