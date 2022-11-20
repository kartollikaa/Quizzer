package com.kartollika.quizzer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kartollika.quizzer.R.string
import com.kartollika.quizzer.navigation.QuizzerNavHost
import com.kartollika.quizzer.player.navigation.navigateToQuizPlayer
import com.kartollika.quizzer.ui.theme.QuizzerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()
      QuizzerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          QuizzerScreen(
            navController = navController,
            modifier = Modifier
              .fillMaxSize()
              .systemBarsPadding()
              .navigationBarsPadding()
              .imePadding(),
            shareFile = this::shareFile
          )
        }

        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val primaryColor = MaterialTheme.colors.primary
        DisposableEffect(systemUiController, useDarkIcons) {
          systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = false
          )
          systemUiController.setNavigationBarColor(color = Color.Transparent)

          onDispose {}
        }
      }

      LaunchedEffect(key1 = Unit) {
        if (intent.data != null) {
          navController.navigateToQuizPlayer(intent.data.toString())
          intent.data = null
        }
      }
    }
  }

  private fun shareFile(file: File) {
    val intentShareFile = Intent(Intent.ACTION_SEND)
    val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
    if (file.exists()) {
      grantUriPermission(
        packageName,
        uri,
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
      )
      intentShareFile.type = "application/pdf"
      intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
      intentShareFile.putExtra(
        Intent.EXTRA_SUBJECT,
        getString(string.share_subject)
      )
      intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(string.share_message))
      startActivity(Intent.createChooser(intentShareFile, getString(string.share_title)))
    }
  }
}

@Composable
private fun QuizzerScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
  shareFile: (File) -> Unit = {}
) {
  QuizzerNavHost(
    navController = navController,
    modifier = modifier,
    shareFile = shareFile
  )
}

@Preview(showBackground = true) @Composable fun DefaultPreview() {
  QuizzerTheme {
    QuizzerScreen(rememberNavController())
  }
}