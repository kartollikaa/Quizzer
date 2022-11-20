package com.kartollika.quizzer.editor.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.editor.R.string
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input

@Composable
internal fun InputQuestion(
  answer: Input,
  onPutAnswer: (String) -> Unit,
  onPutHint: (String, Int) -> Unit
) {

  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    OutlinedTextField(
      value = answer.answer,
      onValueChange = onPutAnswer,
      modifier = Modifier.fillMaxWidth(),
      label = {
        Text(text = stringResource(string.question_input_correct_answer))
      }
    )

    OutlinedTextField(
      value = answer.hints[0],
      onValueChange = { onPutHint(it, 0) },
      modifier = Modifier.fillMaxWidth(),
      label = {
        Text(text = stringResource(string.question_input_hint_1))
      }
    )

    OutlinedTextField(
      value = answer.hints[1],
      onValueChange = { onPutHint(it, 1) },
      modifier = Modifier.fillMaxWidth(),
      label = {
        Text(text = stringResource(string.question_input_hint_2))
      }
    )
  }
}