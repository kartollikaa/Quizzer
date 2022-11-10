package com.kartollika.quizzer.data.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kartollika.quizzer.domain.model.Choice
import com.kartollika.quizzer.domain.model.Input
import com.kartollika.quizzer.domain.model.Slides
import com.kartollika.quizzer.domain.model.StepType
import java.lang.reflect.Type

class StepTypeSerializer: JsonSerializer<StepType> {
  override fun serialize(
    src: StepType,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    return when (src) {
      is Choice -> context.serialize(src, Choice::class.java)
      is Input -> context.serialize(src, Input::class.java)
      is Slides -> context.serialize(src, Slides::class.java)
    }
  }
}