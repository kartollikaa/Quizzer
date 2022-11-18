package com.kartollika.quizzer.player.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.player.QuestionState
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Input
import java.util.Timer
import kotlin.concurrent.timerTask

@Composable
internal fun InputQuestion(
  questionState: QuestionState,
  possibleAnswer: Input,
  answer: Answer.Input?,
  onAnswerTyped: (String) -> Unit,
  modifier: Modifier
) {
  val hints = possibleAnswer.hints
  var hintsVisible by remember(possibleAnswer) {
    mutableStateOf<List<Boolean>>(emptyList())
  }

  questionState.enableCheck = !answer?.value.isNullOrEmpty()

  val clickHandler = { value: String ->
    onAnswerTyped(value)
  }

  val myAnswer = answer?.value?.trim()
  val isError = questionState.checked && myAnswer != possibleAnswer.answer

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .height(120.dp),
      value = answer?.value ?: "",
      onValueChange = clickHandler,
      colors = TextFieldDefaults.outlinedTextFieldColors(
        errorBorderColor = MaterialTheme.colors.error,
        backgroundColor = if (questionState.checked) {
          if (isError) {
            MaterialTheme.colors.error.copy(alpha = 0.12f)
          } else {
            Color.Green.copy(alpha = 0.12f)
          }
        } else {
          TextFieldDefaults.outlinedTextFieldColors().backgroundColor(true).value
        }
      ),
      isError = isError,
      enabled = !questionState.checked
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
      Timer(iteration.toString()).schedule(timerTask {
        hintsVisible = hintsVisible.toMutableList().apply { add(true) }
      }, (iteration + 1) * 5000L)
    }
  }
}