package com.kartollika.quizzer.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.player.R.string

@Composable
internal fun QuestNavigatorController(
  state: QuestionState,
  onPrevious: () -> Unit = {},
  checkAnswer: () -> Unit = {},
  onForward: () -> Unit = {},
  onDone: () -> Unit = {}
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    TextButton(onClick = onPrevious) {
      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(string.accessibility_back))
      Spacer(modifier = Modifier.width(8.dp))
      Text(text = stringResource(string.quiz_back))
    }

    when {
      !state.checked -> {
        TextButton(
          onClick = checkAnswer,
          enabled = state.enableCheck
        ) {
          Text(text = stringResource(string.quiz_check))
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = stringResource(string.accessibility_check_answer))
        }
      }

      state.showDone -> {
        TextButton(onClick = onDone) {
          Text(text = stringResource(string.quiz_done))
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = stringResource(string.accessibility_done))
        }
      }

      else -> {
        TextButton(onClick = onForward) {
          Text(text = stringResource(string.quiz_forward))
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = stringResource(string.accessibility_forward))
        }
      }
    }
  }
}