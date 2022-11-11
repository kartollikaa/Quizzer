package com.kartollika.quizzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kartollika.quizzer.navigation.QuizzerNavHost
import com.kartollika.quizzer.player.navigation.navigateToQuizPlayer
import com.kartollika.quizzer.ui.theme.QuizzerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()
      QuizzerTheme {
        QuizzerScreen(
          navController = navController,
          modifier = Modifier
            .systemBarsPadding()
            .navigationBarsPadding()
            .imePadding()
        )
      }

      val systemUiController = rememberSystemUiController()
      val useDarkIcons = !isSystemInDarkTheme()
      DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )

        // setStatusBarColor() and setNavigationBarColor() also exist

        onDispose {}
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
private fun QuizzerScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  QuizzerNavHost(
    navController = navController,
    modifier = modifier
  )
}

@Preview(showBackground = true) @Composable fun DefaultPreview() {
  QuizzerTheme {
    QuizzerScreen(rememberNavController())
  }
}