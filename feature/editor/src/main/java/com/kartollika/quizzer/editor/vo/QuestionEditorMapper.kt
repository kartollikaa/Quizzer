package com.kartollika.quizzer.editor.vo

import android.util.Base64
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice.Option
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides.Slide
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides

class QuestionEditorMapper {

  fun answerToModel(answer: PossibleAnswerVO): PossibleAnswer = with(answer) {
    return when (this) {
      is Input -> {
        PossibleAnswer.Input(
          hints,
          this.answer
        )
      }
      is SingleChoice -> {
        PossibleAnswer.SingleChoice(
          options = options.map { Option(it.id, it.value, it.linkedQuestionId) },
          correctOption = correctOption
        )
      }

      is Slides -> {
        PossibleAnswer.Slides(
          slides.map {
            Slide(
              text = it.text,
              pictures = it.pictures.map { bitmap ->
                Base64.encodeToString(bitmap.original, Base64.DEFAULT)
              }
            )
          }
        )
      }
      else -> error("Unknown answer type")
    }
  }
}