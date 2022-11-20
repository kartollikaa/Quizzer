package com.kartollika.quizzer.editor

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kartollika.quizzer.editor.R.string

@Composable
internal fun AddNewQuestion(
  addNewQuestion: () -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedButton(
    modifier = modifier,
    onClick = addNewQuestion
  ) {
    Text(text = stringResource(string.quiz_editor_add_question))
  }
}