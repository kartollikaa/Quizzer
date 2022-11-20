package com.kartollika.quizzer.player.vo

import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.util.Base64
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.Place
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice.Option
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides
import com.kartollika.quizzer.domain.model.Question
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.SingleChoice.OptionVO
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Slides.Slide

class QuestionMapper{

  fun mapToQuestionVO(question: Question): QuestionVO {
    return QuestionVO(
      id = question.id,
      questionText = question.questionText,
      answer = mapPossibleAnswer(question.answer)
    )
  }

  private fun mapPossibleAnswer(answer: PossibleAnswer): PossibleAnswerVO {
    return when (answer) {
      is Input -> mapToInputVO(answer)
      is SingleChoice -> mapToSingleChoiceVO(answer)
      is Slides -> mapToSlidesVO(answer)
      is Place -> mapToPlaceVO(answer)
      else -> error("Incompatible possible answer type")
    }
  }

  private fun mapToPlaceVO(answer: Place): PossibleAnswerVO.Place {
    return PossibleAnswerVO.Place(answer.location)
  }

  private fun mapToInputVO(answer: Input) =
    PossibleAnswerVO.Input(answer.hints, answer.answer)

  private fun mapToSlidesVO(answer: Slides) =
    PossibleAnswerVO.Slides(answer.slides.map {
      Slide(
        it.text,
        it.pictures.map { encodedImage ->
          val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size, Options().apply {
            this.inSampleSize = 1
          })
        }
      )
    })

  private fun mapToSingleChoiceVO(answer: SingleChoice) =
    PossibleAnswerVO.SingleChoice(
      options = answer.options.map(::mapSingleChoiceOption),
      correctOption = answer.correctOption
    )

  private fun mapSingleChoiceOption(option: Option): OptionVO {
    return OptionVO(
      option.id,
      option.value,
      option.linkedQuestionId
    )
  }
}