package com.kartollika.quizzer.editor.questions

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.editor.CircleBadge
import com.kartollika.quizzer.editor.R.string
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice.OptionVO

@Composable
internal fun SingleChoiceQuestion(
  questionId: Int,
  answer: SingleChoice,
  onOptionAdded: (Int) -> Unit,
  onOptionDeleted: (Int) -> Unit,
  startLinking: (Int, Int) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    answer.options.forEachIndexed { index, option ->
      SingleChoiceOption(
        questionId,
        answer,
        option,
        onOptionDeleted,
        startLinking
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
      Text(text = stringResource(string.question_single_add_option))
    }
  }
}

@Composable
private fun SingleChoiceOption(
  questionId: Int,
  answer: SingleChoice,
  option: OptionVO,
  deleteOption: (Int) -> Unit,
  startLinking: (Int, Int) -> Unit
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
        val onClick = { startLinking(questionId, option.id) }

        if (option.linkedQuestionId != null) {
          CircleBadge(option.linkedQuestionId!!, modifier = Modifier.clickable { onClick() })
        } else {
          IconButton(onClick = onClick) {
            Icon(
              imageVector = Icons.Default.Link, contentDescription = stringResource(string.accessibility_questions_bind)
            )
          }
        }
      }
    )

    IconButton(onClick = { deleteOption(option.id) }) {
      Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(string.accessibility_delete_option))
    }
  }
}