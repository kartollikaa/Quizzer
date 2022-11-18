package com.kartollika.quizzer.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType
import com.kartollika.quizzer.editor.questions.InputQuestion
import com.kartollika.quizzer.editor.questions.SingleChoiceQuestion
import com.kartollika.quizzer.editor.questions.SlideQuestion
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Empty
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides

@Composable
internal fun QuizQuestion(
  state: QuestionState,
) {
  val onOptionAdded = LocalEditorCallbacks.current.onAddOption
  val onAddSlide = LocalEditorCallbacks.current.onAddSlide
  val onAddPictureOnSlide = LocalEditorCallbacks.current.onAddPictureOnSlide

  when (val answer = state.answer) {
    is SingleChoice -> {
      SingleChoiceQuestion(
        questionId = state.id,
        answer = answer,
        onOptionAdded = onOptionAdded
      )
    }

    is Input -> InputQuestion(
      answer = answer,
      onPutAnswer = { value ->
        answer.answer = value
      }
    )

    is Slides -> {
      SlideQuestion(
        answer = answer,
        state = state,
        onAddSlide = onAddSlide,
        addPictureOnSlide = onAddPictureOnSlide
      )
    }

    Empty -> Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      val callbacks = LocalEditorCallbacks.current
      OutlinedButton(onClick = { callbacks.onQuestionTypeSelected(state.id, QuestionType.Input) }) {
        Text(text = "Text input")
      }

      OutlinedButton(onClick = { callbacks.onQuestionTypeSelected(state.id, QuestionType.SingleChoice) }) {
        Text(text = "Single")
      }

      OutlinedButton(onClick = { callbacks.onQuestionTypeSelected(state.id, QuestionType.Slides) }) {
        Text(text = "Slides")
      }
    }
  }
}