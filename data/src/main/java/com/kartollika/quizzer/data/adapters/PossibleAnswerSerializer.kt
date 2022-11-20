package com.kartollika.quizzer.data.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.Place
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides
import java.lang.reflect.Type

class PossibleAnswerSerializer: JsonSerializer<PossibleAnswer> {
  override fun serialize(
    src: PossibleAnswer,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    val serializeClass = when (src) {
      is SingleChoice -> SingleChoice::class.java
      is Input -> Input::class.java
      is Slides -> Slides::class.java
      is Place -> Place::class.java
      else -> error("Incompatible possible answer type")
    }

    return context.serialize(src, serializeClass)
  }
}