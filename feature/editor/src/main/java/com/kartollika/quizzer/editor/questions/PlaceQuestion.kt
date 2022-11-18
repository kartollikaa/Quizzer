package com.kartollika.quizzer.editor.questions

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Place

@Composable
fun PlaceQuestion(
  questionId: Int,
  answer: Place,
  onLocationSet: (Int) -> Unit
) {
  OutlinedButton(onClick = { onLocationSet(questionId) }) {
    val text = remember {
      if (answer.location == null) {
        "Установить локацию"
      } else {
        val location = answer.location!!
        "Локация: ${location.latitude}:${location.longitude}"
      }
    }
    Text(text = text)
  }
}