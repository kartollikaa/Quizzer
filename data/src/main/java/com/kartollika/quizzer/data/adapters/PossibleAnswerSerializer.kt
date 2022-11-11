package com.kartollika.quizzer.data.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.MultipleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import java.lang.reflect.Type

class PossibleAnswerSerializer: JsonSerializer<PossibleAnswer> {
  override fun serialize(
    src: PossibleAnswer,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    return when (src) {
      is SingleChoice -> context.serialize(src, SingleChoice::class.java)
      is MultipleChoice -> context.serialize(src, MultipleChoice::class.java)
      is Input -> context.serialize(src, Input::class.java)
    }
  }
}