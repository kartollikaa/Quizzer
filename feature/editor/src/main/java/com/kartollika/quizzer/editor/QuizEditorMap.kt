package com.kartollika.quizzer.editor

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.maps.android.compose.GoogleMap

@Composable
fun QuizEditorMapRoute(
  viewModel: QuizEditorViewModel = hiltViewModel()
) {
  GoogleMap()
}