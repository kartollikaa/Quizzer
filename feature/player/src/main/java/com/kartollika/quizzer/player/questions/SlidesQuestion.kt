package com.kartollika.quizzer.player.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Slides

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun SlidesQuestion(
  possibleAnswer: Slides,
  modifier: Modifier
) {
  HorizontalPager(
    modifier = modifier,
    count = possibleAnswer.slides.size,
    verticalAlignment = Alignment.Top
  ) { page ->
    val slide = possibleAnswer.slides[page]
    Column(
      modifier = Modifier.wrapContentHeight()
    ) {
      slide.text?.let { text ->
        Text(text = text, style = MaterialTheme.typography.h5)
      }

      slide.pictures?.forEach {
        Image(
          modifier = Modifier.fillMaxWidth(),
          bitmap = it.asImageBitmap(),
          contentDescription = null
        )
      }
    }
  }
}