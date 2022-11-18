package com.kartollika.quizzer.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CircleBadge(
  value: Int,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp)
      .background(color = MaterialTheme.colors.primary, shape = CircleShape)
      .clip(CircleShape)
      .padding(horizontal = 8.dp), contentAlignment = Center
  ) {
    ProvideTextStyle(
      value = LocalTextStyle.current.copy(
        color = contentColorFor(
          backgroundColor = MaterialTheme.colors.primary
        )
      )
    ) {
      Text(text = value.toString())
    }
  }
}