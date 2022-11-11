package com.kartollika.quizzer.player.vo

import android.graphics.BitmapFactory
import android.util.Base64
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.MultipleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides
import com.kartollika.quizzer.domain.model.Question

class QuestionMapper {
  fun mapToQuestionVO(question: Question): QuestionVO {
    return QuestionVO(
      id = question.id,
      questionText = question.questionText,
      answer = mapPossibleAnswer(question.answer)
    )
  }

  private fun mapPossibleAnswer(answer: PossibleAnswer): PossibleAnswerVO {
    return when (answer) {
      is Input -> PossibleAnswerVO.Input(answer.hints, answer.answer)
      is MultipleChoice -> PossibleAnswerVO.MultipleChoice(answer.options)
      is SingleChoice -> PossibleAnswerVO.SingleChoice(answer.options, answer.correctOption)
      is Slides -> PossibleAnswerVO.Slides(answer.slides.map {
        PossibleAnswerVO.Slides.Slide(
          it.text,
          it.pictures?.map { encodedImage ->
            val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
          }
        )
      })
    }
  }
}