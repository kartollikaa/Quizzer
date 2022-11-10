package com.kartollika.quizzer.data.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kartollika.quizzer.domain.model.Choice
import com.kartollika.quizzer.domain.model.Input
import com.kartollika.quizzer.domain.model.Slides
import com.kartollika.quizzer.domain.model.StepType
import java.lang.reflect.Type

class StepTypeDeserializer: JsonDeserializer<StepType> {
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): StepType {
    return when (json.asJsonObject["name"].asString) {
      "slides" -> {
        context.deserialize(json, Slides::class.java)
      }
      "input" -> {
        context.deserialize(json, Input::class.java)
      }

      "choice" -> {
        context.deserialize(json, Choice::class.java)
      }

      else -> error("Unknown step type")
    }
  }
}