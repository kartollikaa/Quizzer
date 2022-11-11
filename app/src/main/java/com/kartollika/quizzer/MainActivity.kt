package com.kartollika.quizzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kartollika.quizzer.navigation.QuizzerNavHost
import com.kartollika.quizzer.player.navigation.navigateToQuizPlayer
import com.kartollika.quizzer.ui.theme.QuizzerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val navController = rememberNavController()
      QuizzerTheme {
        QuizzerScreen(navController)
      }

      LaunchedEffect(key1 = Unit) {
        if (intent.data != null) {
          navController.navigateToQuizPlayer(intent.data.toString())
        }
      }
    }
  }
}

@Composable
private fun QuizzerScreen(navController: NavHostController) {
  QuizzerNavHost(
    navController = navController,
  )
}

@Preview(showBackground = true) @Composable fun DefaultPreview() {
  QuizzerTheme {
    QuizzerScreen(rememberNavController())
  }
}