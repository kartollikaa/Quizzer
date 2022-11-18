package com.kartollika.quizzer.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO
import java.io.File

internal data class QuestionState(
  val id: Int
) {
  var questionText by mutableStateOf("")
  var answer by mutableStateOf<PossibleAnswerVO>(PossibleAnswerVO.Empty)
}

internal data class QuizEditorState(
  val quizTitle: String = "",
) {
  var questions by mutableStateOf<List<QuestionState>>(emptyList())
  var fileToShare by mutableStateOf<File?>(null)
  var quizGenerating by mutableStateOf(false)
}