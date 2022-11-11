package com.kartollika.quizzer.player.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.PossibleAnswer
import java.util.Timer
import kotlin.concurrent.timerTask

@Composable
fun InputQuestion(
  possibleAnswer: PossibleAnswer.Input,
  answer: Answer.Input?,
  onAnswerTyped: (String) -> Unit,
  modifier: Modifier
) {
  val hints = possibleAnswer.hints
  var hintsVisible by remember(possibleAnswer) {
    mutableStateOf<List<Boolean>>(emptyList())
  }

  val (input, onInputChanged) = remember(answer) { mutableStateOf(answer?.value ?: "") }

  val clickHandler = { value: String ->
    onInputChanged(value)
    onAnswerTyped(value)
  }

  Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .height(120.dp),
      value = input,
      onValueChange = clickHandler,
    )

    Spacer(modifier = Modifier.height(16.dp))

    hintsVisible.forEachIndexed { index, visible ->
      if (visible) {
        Text(text = hints[index])
      }
    }
  }

  LaunchedEffect(key1 = Unit) {
    repeat(hints.size) { iteration ->
      Timer().schedule(timerTask {
        hintsVisible = hintsVisible.toMutableList().apply { add(true) }
      }, (iteration + 1) * 5000L)
    }
  }
}