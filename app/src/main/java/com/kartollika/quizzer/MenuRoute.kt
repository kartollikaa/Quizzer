package com.kartollika.quizzer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kartollika.quizzer.R.string

const val menuRoute = "menu"

fun NavGraphBuilder.menuScreen(
  navigateToPlayer: (quizUri: String) -> Unit,
  navigateToEditor: () -> Unit,
) {
  composable(
    route = menuRoute
  ) {
    MenuRoute(
      navigateToPlayer = navigateToPlayer,
      navigateToEditor = navigateToEditor,
    )
  }
}

@Composable
fun MenuRoute(
  navigateToPlayer: (quizUri: String) -> Unit,
  navigateToEditor: () -> Unit,
) {
  val launcher = rememberLauncherForActivityResult(
    OpenDocument()
  ) { uri: Uri? ->
    uri?.let { navigateToPlayer(it.toString()) }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(text = stringResource(string.quizzer_menu_title))
        }
      )
    },
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Button(onClick = {
        launcher.launch(arrayOf("*/*"))
      }) {
        Text(text = stringResource(string.play_quiz))
      }

      Button(onClick = navigateToEditor) {
        Text(text = stringResource(string.create_quiz))
      }
    }
  }
}