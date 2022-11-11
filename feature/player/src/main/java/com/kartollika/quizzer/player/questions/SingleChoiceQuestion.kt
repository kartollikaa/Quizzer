package com.kartollika.quizzer.player.questions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.player.QuestionState

@Composable
fun SingleChoiceQuestion(
  questionState: QuestionState,
  possibleAnswer: SingleChoice,
  answer: Answer.SingleChoice?,
  onAnswerSelected: (String) -> Unit,
  modifier: Modifier,
) {
  val options = possibleAnswer.options
  val selected = answer?.answer
  questionState.enableCheck = answer != null

  Column(modifier = modifier) {
    options.forEach { option ->
      val onClickHandle = {
        onAnswerSelected(option)
      }
      val optionSelected = option == selected

      val answerBorderColor = if (optionSelected) {
        MaterialTheme.colors.primary.copy(alpha = 0.5f)
      } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
      }
      val answerBackgroundColor = when {
        questionState.checked -> {
          if (possibleAnswer.correctOption == option) {
            Color.Green.copy(alpha = 0.12f)
          } else if (optionSelected) {
            MaterialTheme.colors.error
          } else {
            MaterialTheme.colors.background
          }
        }
        else -> {
          if (optionSelected) {
            MaterialTheme.colors.primary.copy(alpha = 0.12f)
          } else {
            MaterialTheme.colors.background
          }
        }
      }
      Surface(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
          width = 1.dp,
          color = answerBorderColor
        ),
        modifier = Modifier.padding(vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .selectable(
              selected = optionSelected,
              onClick = onClickHandle,
              enabled = !questionState.checked,
            )
            .background(answerBackgroundColor)
            .padding(vertical = 16.dp, horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = option
          )

          RadioButton(
            selected = optionSelected,
            onClick = onClickHandle,
            colors = RadioButtonDefaults.colors(
              selectedColor = MaterialTheme.colors.primary
            ),
            enabled = !questionState.checked,
          )
        }
      }
    }
  }
}