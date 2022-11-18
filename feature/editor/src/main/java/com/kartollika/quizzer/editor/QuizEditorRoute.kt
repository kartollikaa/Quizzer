package com.kartollika.quizzer.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import java.io.File

@Composable fun QuizEditorRoute(
  modifier: Modifier = Modifier,
  viewModel: QuizEditorViewModel = hiltViewModel(),
  shareFile: (File) -> Unit
) {
  val state by viewModel.uiState.collectAsState()

  LaunchedEffect(state.fileToShare) {
    if (state.fileToShare == null) return@LaunchedEffect
    shareFile(state.fileToShare!!)
  }

  val quizEditorCallbacks = EditorCallbacks(
    onAddOption = viewModel::addOption,
    onQuestionNameChanged = viewModel::setQuestionName,
    onQuestionDelete = viewModel::onQuestionDelete,
    onQuestionTypeSelected = viewModel::onQuestionTypeSelected,
    onAddSlide = viewModel::addSlide,
    onAddPictureOnSlide = viewModel::onAddPictureOnSlide
  )

  CompositionLocalProvider(LocalEditorCallbacks provides quizEditorCallbacks) {
    QuizEditor(
      modifier = modifier,
      state = state,
      addNewQuestion = viewModel::addNewQuestion,
      generateQuiz = viewModel::generateQuiz,
    )
  }
}
/*

@Preview(showBackground = true) @Composable fun QuizEditorScreenPreview() {
  QuizEditor(
    state = quizEditorPreviewState
  )
}
*/

/*
private val quizEditorPreviewState = QuizEditorState(
  quizTitle = "My Quiz"
).apply {
  questions = listOf(
    QuestionState(id = 1).apply {
      questionText = "Say my name"
      answer = SingleChoice(listOf("Heisenberg", "Tuco", "Jessie"), correctOption = "Heisenberg")
    },

    QuestionState(id = 2).apply {
      questionText = "Type my name"
      answer = Input(
        hints = listOf("Knock-knock"), answer = "Heisenberg"
      )
    },

    QuestionState(id = 3).apply {
      questionText = "Say my name"
      answer = Slides(
        slides = listOf(
          Slide("First slide", listOf("1", "2")),
          Slide("Second slide", listOf("1"))
        )
      )
    }
  )
}*/
