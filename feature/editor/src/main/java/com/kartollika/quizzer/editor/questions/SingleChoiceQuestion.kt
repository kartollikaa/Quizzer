package com.kartollika.quizzer.editor.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice.OptionVO

@Composable
internal fun SingleChoiceQuestion(
  questionId: Int,
  answer: SingleChoice,
  onOptionAdded: (Int) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    answer.options.forEachIndexed { index, option ->
      SingleChoiceOption(
        answer,
        option,
      )
    }

    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(CenterHorizontally),
      onClick = {
        onOptionAdded(questionId)
      }
    ) {
      Text(text = "Добавить ответ")
    }
  }
}

@Composable
private fun SingleChoiceOption(
  answer: SingleChoice,
  option: OptionVO,
) {
  Row(modifier = Modifier.fillMaxWidth()) {
    RadioButton(
      selected = option.id == answer.correctOption && option.isNotEmpty(),
      onClick = {
        if (option.value.isEmpty()) return@RadioButton
        answer.correctOption = option.id
      }
    )

    OutlinedTextField(
      value = option.value,
      onValueChange = { value ->
        option.value = value
      },
      modifier = Modifier.weight(1f),
      trailingIcon = {
        IconButton(onClick = { /*TODO*/ }) {
          Icon(
            imageVector = Icons.Default.Link, contentDescription = "Связать"
          )
        }
      }
    )
  }
}