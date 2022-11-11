package com.kartollika.quizzer.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.PossibleAnswer.Input
import com.kartollika.quizzer.domain.model.PossibleAnswer.MultipleChoice
import com.kartollika.quizzer.domain.model.PossibleAnswer.SingleChoice
import com.kartollika.quizzer.player.questions.InputQuestion
import com.kartollika.quizzer.player.questions.SingleChoiceQuestion

@Composable
fun QuestionContent(
  questionState: QuestionState,
  answer: Answer<*>?,
  onAnswer: (Answer<*>) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
  ) {
    val possibleAnswer = questionState.question.answer

    item {
      Spacer(modifier = Modifier.height(16.dp))
      QuestionTitle(title = questionState.question.questionText)
      Spacer(modifier = Modifier.height(24.dp))

      when (possibleAnswer) {
        is MultipleChoice -> {
        }
        is SingleChoice -> {
          SingleChoiceQuestion(
            questionState = questionState,
            possibleAnswer = possibleAnswer,
            answer = answer as Answer.SingleChoice?,
            onAnswerSelected = { answer -> onAnswer(Answer.SingleChoice(answer)) },
            modifier = Modifier.fillParentMaxWidth()
          )
        }
        is Input -> {
          InputQuestion(
            possibleAnswer = possibleAnswer,
            answer = answer as Answer.Input?,
            onAnswerTyped = { answer -> onAnswer(Answer.Input(answer)) },
            modifier = Modifier.fillParentMaxWidth()
          )
        }
      }
    }
  }
}

@Composable
private fun QuestionTitle(title: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        color = Color.Gray,
        shape = MaterialTheme.shapes.medium
      )
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.h4,
      color = MaterialTheme.colors.onSurface,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 24.dp, horizontal = 16.dp)
    )
  }
}