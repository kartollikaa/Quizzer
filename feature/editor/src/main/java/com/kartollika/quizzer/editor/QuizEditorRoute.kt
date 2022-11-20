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
  shareFile: (File) -> Unit,
  goBack: () -> Unit
) {
  val state by viewModel.uiState.collectAsState()

  LaunchedEffect(state.fileToShare) {
    if (state.fileToShare == null) return@LaunchedEffect
    shareFile(state.fileToShare!!)
    state.fileToShare = null
  }

  val quizEditorCallbacks = EditorCallbacks(
    onAddOption = viewModel::addOption,
    onQuestionNameChanged = viewModel::setQuestionName,
    onQuestionDelete = viewModel::onQuestionDelete,
    onQuestionTypeSelected = viewModel::onQuestionTypeSelected,
    onAddSlide = viewModel::addSlide,
    onAddPictureOnSlide = viewModel::onAddPictureOnSlide,
    onLocationSet = viewModel::onLocationSet,
    onOptionDeleted = viewModel::deleteOption,
    startLinking = viewModel::startLinking,
    endLinking = viewModel::endLinking,
    cancelLinking = viewModel::cancelLinking,
    deleteSlide = viewModel::deleteSlide,
    locationEnabled = viewModel::locationEnabled
  )

  CompositionLocalProvider(LocalEditorCallbacks provides quizEditorCallbacks) {
    QuizEditor(
      modifier = modifier,
      state = state,
      addNewQuestion = viewModel::addNewQuestion,
      generateQuiz = viewModel::generateQuiz,
      goBack = goBack
    )
  }
}
