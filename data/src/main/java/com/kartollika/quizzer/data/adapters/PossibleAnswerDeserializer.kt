package com.kartollika.quizzer.data.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.MultipleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides
import java.lang.reflect.Type

class PossibleAnswerDeserializer: JsonDeserializer<PossibleAnswer> {
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): PossibleAnswer {
    return when (json.asJsonObject["name"].asString) {
      "input" -> context.deserialize(json, Input::class.java)
      "single_choice" -> context.deserialize(json, SingleChoice::class.java)
      "multiple_choice" -> context.deserialize(json, MultipleChoice::class.java)
      "slides" -> context.deserialize(json, Slides::class.java)
      else -> error("Unknown step type")
    }
  }
}