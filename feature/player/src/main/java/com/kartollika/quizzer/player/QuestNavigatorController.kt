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
import androidx.compose.ui.unit.dp

@Composable
fun QuestNavigatorController(
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
      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
      Spacer(modifier = Modifier.width(8.dp))
      Text(text = "Назад")
    }

    when {
      !state.checked -> {
        TextButton(
          onClick = checkAnswer,
          enabled = state.enableCheck
        ) {
          Text(text = "Проверить")
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Проверить")
        }
      }

      state.showDone -> {
        TextButton(onClick = onDone) {
          Text(text = "Готово")
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Готово")
        }
      }

      else -> {
        TextButton(onClick = onForward) {
          Text(text = "Вперед")
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Вперед")
        }
      }
    }
  }
}