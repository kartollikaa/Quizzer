package com.kartollika.quizzer.player.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.kartollika.quizzer.domain.model.PossibleAnswer
import com.kartollika.quizzer.player.QuestionState
import com.kartollika.quizzer.player.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlidesQuestion(
  questionState: QuestionState,
  possibleAnswer: PossibleAnswer.Slides,
  modifier: Modifier
) {
  HorizontalPager(
    modifier = modifier,
    count = possibleAnswer.slides.size,
    verticalAlignment = Alignment.Top
  ) { page ->
    val slide = possibleAnswer.slides[page]
    Column(modifier = Modifier.wrapContentHeight()) {
      slide.text?.let { text ->
        Text(text = text, style = MaterialTheme.typography.h5)
      }
      slide.pictures?.forEach {
        Image(painter = painterResource(id = R.drawable.stock), contentDescription = null)
      }
    }
  }
}