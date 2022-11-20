package com.kartollika.quizzer.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.Location
import com.kartollika.quizzer.player.questions.InputQuestion
import com.kartollika.quizzer.player.questions.PlaceQuestion
import com.kartollika.quizzer.player.questions.SingleChoiceQuestion
import com.kartollika.quizzer.player.questions.SlidesQuestion
import com.kartollika.quizzer.player.vo.PossibleAnswerVO
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Input
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Place
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Slides
import java.util.Locale

@Composable
internal fun QuestionContent(
  questionState: QuestionState,
  answer: Answer<*>?,
  onAnswer: (Answer<*>) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(16.dp)
  ) {
    val possibleAnswer = questionState.question.answer

    item {
      Spacer(modifier = Modifier.height(16.dp))
      QuestionTitle(title = questionState.question.questionText)
      Spacer(modifier = Modifier.height(24.dp))

      when (possibleAnswer) {
        is PossibleAnswerVO.SingleChoice -> {
          SingleChoiceQuestion(
            questionState = questionState,
            possibleAnswer = possibleAnswer,
            answer = answer as Answer.SingleChoice?,
            onAnswerSelected = { answer ->
              onAnswer(
                Answer.SingleChoice(
                  answer,
                  questionId = questionState.question.id
                )
              )
            },
            modifier = Modifier
          )
        }
        is Input -> {
          InputQuestion(
            questionState = questionState,
            possibleAnswer = possibleAnswer,
            answer = answer as Answer.Input?,
            onAnswerTyped = { answer ->
              onAnswer(
                Answer.Input(
                  answer,
                  questionId = questionState.question.id
                )
              )
            },
            modifier = Modifier
          )
        }
        is Slides -> {
          SlidesQuestion(
            possibleAnswer = possibleAnswer,
            modifier = Modifier.fillMaxSize()
          )
        }
        is Place -> {
          PlaceQuestion(
            state = questionState,
            possibleAnswer = possibleAnswer,
            modifier = Modifier.fillMaxSize(),
            navigateToMap = { location ->
              openMapWithLocation(context, location)
            },
            onAnswer = { answer ->
              questionState.checked = true
              onAnswer(Answer.Place(answer, questionId = questionState.question.id))
            },
            answer = answer as? Answer.Place
          )
        }
      }
    }
  }
}

private fun openMapWithLocation(context: Context, location: Location) {
  val uri = String.format(Locale.ENGLISH, "geo:%f,%f", location.latitude, location.longitude)
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
  context.startActivity(intent)
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