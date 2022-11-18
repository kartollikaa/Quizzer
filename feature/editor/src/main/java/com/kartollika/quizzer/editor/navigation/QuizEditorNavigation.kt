package com.kartollika.quizzer.editor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kartollika.quizzer.editor.QuizEditorMapRoute
import com.kartollika.quizzer.editor.QuizEditorRoute
import java.io.File

const val quizEditorRoute = "quiz_editor_route"
const val quizEditorGraph = "quiz_editor_graph"

const val quizEditorMapRoute = "quiz_editor_map_route"
const val quizEditorMapQuestionIdArg = "question_id"

fun NavController.navigateToQuizEditor() {
  navigate(quizEditorGraph)
}

fun NavController.navigateToQuizEditorMap(questionId: Int) {
  navigate("$quizEditorMapRoute/$questionId")
}

fun NavGraphBuilder.quizEditorGraph(
  shareFile: (File) -> Unit,
  navigateToMap: (Int) -> Unit,
  goBack: () -> Unit
) {
  navigation(startDestination = quizEditorRoute, route = quizEditorGraph) {
    composable(route = quizEditorRoute) {
      QuizEditorRoute(
        shareFile = shareFile,
        navigateToMap = navigateToMap,
        goBack = goBack
      )
    }

    composable(
      route = "$quizEditorMapRoute/{$quizEditorMapQuestionIdArg}",
      arguments = listOf(
        navArgument(quizEditorMapQuestionIdArg) {
          type = NavType.IntType
        }
      )
    ) {
      QuizEditorMapRoute()
    }
  }
}
