package com.kartollika.quizzer.player.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kartollika.quizzer.player.QuizPlayerRoute

const val quizFileUriArg = "quizFileUriArg"

const val quizPlayerRoute = "quiz_player_route"
val routeArg = { uri: String -> "$quizPlayerRoute/$uri" }

fun NavController.navigateToQuizPlayer(quizFileUri: String) {
  navigate(routeArg(Uri.encode(quizFileUri)))
}

fun NavGraphBuilder.quizPlayerScreen(onBack: () -> Unit) {
  composable(
    route = "$quizPlayerRoute/{$quizFileUriArg}",
    arguments = listOf(
      navArgument(quizFileUriArg) {
        type = NavType.StringType
      }
    )
  ) {
    QuizPlayerRoute(
      onBack = onBack
    )
  }
}
