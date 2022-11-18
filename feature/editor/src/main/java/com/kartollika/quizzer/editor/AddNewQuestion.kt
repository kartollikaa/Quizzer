package com.kartollika.quizzer.editor

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun AddNewQuestion(
  addNewQuestion: () -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedButton(
    modifier = modifier,
    onClick = addNewQuestion
  ) {
    Text(text = "Добавить вопрос")
  }
}