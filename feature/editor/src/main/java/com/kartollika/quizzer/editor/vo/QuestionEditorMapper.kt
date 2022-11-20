package com.kartollika.quizzer.editor.vo

import android.util.Base64
import com.kartollika.quizzer.domain.model.Location
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice.Option
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides.Slide
import com.kartollika.quizzer.domain.model.Question
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.editor.QuizEditorState
import com.kartollika.quizzer.editor.R
import com.kartollika.quizzer.editor.vo.InvalidQuizException.ChoiceOptionEmptyException
import com.kartollika.quizzer.editor.vo.InvalidQuizException.InputEmptyException
import com.kartollika.quizzer.editor.vo.InvalidQuizException.LocationEmptyException
import com.kartollika.quizzer.editor.vo.InvalidQuizException.NoSlidesException
import com.kartollika.quizzer.editor.vo.InvalidQuizException.OptionNotSelectedException
import com.kartollika.quizzer.editor.vo.InvalidQuizException.QuestionNotSelectedException
import com.kartollika.quizzer.editor.vo.InvalidQuizException.SlideEmptyException
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Empty
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Place
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides

class QuestionEditorMapper {

  internal fun quizStateToModel(quizEditorState: QuizEditorState): Quiz = with(quizEditorState) {
    return@with Quiz(
      title = quizTitle,
      questions = questions.map {
        Question(it.id, it.questionText, answerToModel(it.id, it.answer))
      }
    )
  }

  private fun answerToModel(questionId: Int, answer: PossibleAnswerVO): PossibleAnswer =
    with(answer) {
      return when (this) {
        is Input -> {
          if (this.answer.isEmpty()) throw InputEmptyException(
            QuizInvalidMessage(
              R.string.invalid_quiz_input_empty,
              questionId
            )
          )
          PossibleAnswer.Input(
            hints,
            this.answer
          )
        }
        is SingleChoice -> {
          if (options.any { it.value.isEmpty() }) throw ChoiceOptionEmptyException(
            QuizInvalidMessage(
              R.string.invalid_quiz_single_choice_empty,
              questionId
            )
          )
          if (correctOption == -1) throw OptionNotSelectedException(
            QuizInvalidMessage(
              R.string.invalid_quiz_single_choice_no_correct_option,
              questionId
            )
          )
          PossibleAnswer.SingleChoice(
            options = options.map { Option(it.id, it.value, it.linkedQuestionId) },
            correctOption = correctOption
          )
        }

        is Slides -> {
          if (slides.isEmpty()) throw NoSlidesException(
            QuizInvalidMessage(
              R.string.invalid_quiz_no_slides,
              questionId
            )
          )
          if (slides.any { it.pictures.isEmpty() && it.text.isEmpty() }) throw SlideEmptyException(
            QuizInvalidMessage(
              R.string.invalid_quiz_one_of_slides_empty, questionId
            )
          )

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

        is Place -> {
          val location = location ?: throw LocationEmptyException(
            QuizInvalidMessage(
              R.string.invalid_quiz_location_empty,
              questionId
            )
          )
          PossibleAnswer.Place(
            Location(location.latitude, location.longitude)
          )
        }

        Empty -> throw QuestionNotSelectedException(QuizInvalidMessage(R.string.invalid_quiz_no_question_selected, questionId))
      }
    }
}