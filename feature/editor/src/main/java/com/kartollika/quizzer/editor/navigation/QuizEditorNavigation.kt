package com.kartollika.quizzer.editor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kartollika.quizzer.editor.QuizEditorRoute
import java.io.File

const val quizEditorRoute = "quiz_editor_route"

fun NavController.navigateToQuizEditor() {
  navigate(quizEditorRoute)
}

fun NavGraphBuilder.quizEditorScreen(
  shareFile: (File) -> Unit
) {
  composable(route = quizEditorRoute) {
    QuizEditorRoute(
      shareFile = shareFile
    )
  }
}
