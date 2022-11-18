package com.kartollika.quizzer.editor.questions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Input

@Composable
internal fun InputQuestion(
  answer: Input,
  onPutAnswer: (String) -> Unit
) {
  OutlinedTextField(
    value = answer.answer,
    onValueChange = onPutAnswer,
    modifier = Modifier.fillMaxWidth(),
    label = {
      Text(text = "Правильный ответ")
    }
  )
}