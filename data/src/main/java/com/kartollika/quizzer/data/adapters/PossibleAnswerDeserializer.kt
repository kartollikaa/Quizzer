package com.kartollika.quizzer.data.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Companion.INPUT_TYPE
import com.kartollika.quizzer.domain.model.PossibleAnswer.Companion.PLACE_TYPE
import com.kartollika.quizzer.domain.model.PossibleAnswer.Companion.SINGLE_CHOICE_TYPE
import com.kartollika.quizzer.domain.model.PossibleAnswer.Companion.SLIDES_TYPE
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.Place
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides
import java.lang.reflect.Type

class PossibleAnswerDeserializer: JsonDeserializer<PossibleAnswer> {
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): PossibleAnswer {
    val deserializeClass = when (json.asJsonObject["name"].asString) {
      INPUT_TYPE -> Input::class.java
      SINGLE_CHOICE_TYPE -> SingleChoice::class.java
      SLIDES_TYPE -> Slides::class.java
      PLACE_TYPE -> Place::class.java
      else -> error("Unknown step type")
    }

    return context.deserialize(json, deserializeClass)
  }
}