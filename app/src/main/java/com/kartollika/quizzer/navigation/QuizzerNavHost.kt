package com.kartollika.quizzer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kartollika.quizzer.menuRoute
import com.kartollika.quizzer.menuScreen
import com.kartollika.quizzer.player.navigation.navigateToQuizPlayer
import com.kartollika.quizzer.player.navigation.quizPlayerScreen

@Composable
fun QuizzerNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier,
  startDestination: String = menuRoute
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {
    menuScreen(
      navigateToPlayer = { quizUri ->
        navController.navigateToQuizPlayer(quizUri)
      }
    )
    quizPlayerScreen(
      goBack = {
        navController.popBackStack()
      }
    )
  }
}
