package com.kartollika.quizzer.editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType.Location
import com.kartollika.quizzer.editor.questions.InputQuestion
import com.kartollika.quizzer.editor.questions.PlaceQuestion
import com.kartollika.quizzer.editor.questions.SingleChoiceQuestion
import com.kartollika.quizzer.editor.questions.SlideQuestion
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Empty
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Place
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides

@Composable
internal fun QuizQuestion(
  state: QuestionState,
) {
  val onOptionAdded = LocalEditorCallbacks.current.onAddOption
  val onAddSlide = LocalEditorCallbacks.current.onAddSlide
  val onAddPictureOnSlide = LocalEditorCallbacks.current.onAddPictureOnSlide
  val onLocationSet = LocalEditorCallbacks.current.onLocationSet
  val onOptionDeleted = LocalEditorCallbacks.current.onOptionDeleted
  val startLinking = LocalEditorCallbacks.current.startLinking
  val deleteSlide = LocalEditorCallbacks.current.deleteSlide
  val locationEnabled = LocalEditorCallbacks.current.locationEnabled

  when (val answer = state.answer) {
    is SingleChoice -> {
      SingleChoiceQuestion(
        questionId = state.id,
        answer = answer,
        onOptionAdded = onOptionAdded,
        onOptionDeleted = { optionId ->
          onOptionDeleted(state.id, optionId)
        },
        startLinking = startLinking
      )
    }

    is Input -> InputQuestion(
      answer = answer,
      onPutAnswer = { value ->
        answer.answer = value
      },
      onPutHint = { value, hintNumber ->
        answer.hints = answer.hints.toMutableList().apply {
          set(hintNumber, value)
        }
      }
    )

    is Slides -> {
      SlideQuestion(
        answer = answer,
        state = state,
        onAddSlide = onAddSlide,
        addPictureOnSlide = onAddPictureOnSlide,
        deleteSlide = deleteSlide
      )
    }

    is Place -> {
      PlaceQuestion(
        questionId = state.id,
        answer = answer,
        onLocationSet = {
          onLocationSet(state.id)
        },
        locationEnabled = locationEnabled
      )
    }

    Empty -> FlowRow(
      modifier = Modifier.fillMaxWidth(),
      mainAxisSpacing = 16.dp
    ) {
      val callbacks = LocalEditorCallbacks.current
      OutlinedButton(onClick = { callbacks.onQuestionTypeSelected(state.id, QuestionType.Input) }) {
        Text(text = "Text input")
      }

      OutlinedButton(onClick = {
        callbacks.onQuestionTypeSelected(
          state.id,
          QuestionType.SingleChoice
        )
      }) {
        Text(text = "Single")
      }

      OutlinedButton(onClick = {
        callbacks.onQuestionTypeSelected(
          state.id,
          QuestionType.Slides
        )
      }) {
        Text(text = "Slides")
      }

      OutlinedButton(onClick = { callbacks.onQuestionTypeSelected(state.id, Location) }) {
        Text(text = "Location")
      }
    }
  }
}