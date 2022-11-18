package com.kartollika.quizzer.editor.vo

import android.util.Base64
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice.Option
import com.kartollika.quizzer.domain.model.PossibleAnswer.Slides.Slide
import com.kartollika.quizzer.domain.model.Question
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.editor.QuizEditorState
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Empty
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input
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

  private fun answerToModel(questionId: Int, answer: PossibleAnswerVO): PossibleAnswer = with(answer) {
    return when (this) {
      is Input -> {
        if (this.answer.isEmpty()) error("Input field is empty in $questionId")
        PossibleAnswer.Input(
          hints,
          this.answer
        )
      }
      is SingleChoice -> {
        if (options.any { it.value.isEmpty() }) error("Choice option is empty in $questionId")
        if (correctOption == -1) error("Correct option not selected in $questionId")
        PossibleAnswer.SingleChoice(
          options = options.map { Option(it.id, it.value, it.linkedQuestionId) },
          correctOption = correctOption
        )
      }

      is Slides -> {
        if (slides.isEmpty()) error("No slides in $questionId")
        if (slides.any { it.pictures.isEmpty() && it.text.isEmpty() }) error("Slide is empty in $questionId")

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
      Empty -> {
        error("Question not selected in $questionId")
      }

      else -> error("Unknown answer type")
    }
  }
}