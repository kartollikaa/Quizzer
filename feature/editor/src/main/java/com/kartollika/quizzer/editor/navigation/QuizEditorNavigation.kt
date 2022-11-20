package com.kartollika.quizzer.editor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kartollika.quizzer.editor.QuizEditorRoute
import java.io.File

const val quizEditorRoute = "quiz_editor_route"
const val quizEditorGraph = "quiz_editor_graph"

fun NavController.navigateToQuizEditor() {
  navigate(quizEditorGraph)
}

fun NavGraphBuilder.quizEditorGraph(
  shareFile: (File) -> Unit,
  goBack: () -> Unit,
) {
  navigation(startDestination = quizEditorRoute, route = quizEditorGraph) {
    composable(route = quizEditorRoute) {
      QuizEditorRoute(
        shareFile = shareFile,
        goBack = goBack
      )
    }
  }
}