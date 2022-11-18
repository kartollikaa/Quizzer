package com.kartollika.quizzer.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable internal fun QuizEditor(
  viewModel: QuizEditorViewModel = hiltViewModel(),
  state: QuizEditorState,
  modifier: Modifier = Modifier,
  addNewQuestion: () -> Unit = {},
  generateQuiz: () -> Unit = {},
) {
  val scrollableState = rememberLazyListState()

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = modifier.fillMaxSize(),
      state = scrollableState,
      contentPadding = PaddingValues(
        start = 16.dp, top = 16.dp, end = 16.dp, bottom = 128.dp
      ), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      itemsIndexed(items = state.questions,
        key = { _, item -> item.id }
      ) { index, question: QuestionState ->
        Question(
          state = question,
          index = index + 1,
          modifier = Modifier
            .fillMaxSize()
            .animateItemPlacement(),
        )
      }

      item {
        AddNewQuestion(
          addNewQuestion = addNewQuestion
        )
      }
    }

    GenerateQuiz(
      modifier = Modifier.align(BottomCenter),
      visible = state.questions.isNotEmpty(),
      generateQuiz = generateQuiz
    )
  }
}

@Composable fun GenerateQuiz(
  visible: Boolean,
  modifier: Modifier = Modifier,
  generateQuiz: () -> Unit,
) {
  val transition = updateTransition(visible)
  val offset by transition.animateDp { buttonVisible ->
    if (buttonVisible) {
      -32.dp
    } else {
      200.dp
    }
  }

  ExtendedFloatingActionButton(
    modifier = modifier.offset(y = offset),
    text = {
      Text(text = "Готово")
    },
    onClick = { generateQuiz() },
    icon = {
      Icon(imageVector = Icons.Default.Check, contentDescription = null)
    }
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable private fun Question(
  state: QuestionState,
  index: Int = 1,
  modifier: Modifier = Modifier,
) {
  var expanded by remember { mutableStateOf(true) }

  val questionDelete = LocalEditorCallbacks.current.onQuestionDelete
  Card(modifier = modifier, onClick = {
    expanded = !expanded
  }) {
    Column(
      modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .animateContentSize()
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
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
            Text(text = index.toString())
          }
        }

        AnimatedVisibility(
          visible = !expanded,
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = state.questionText,
          )
        }

        if (expanded) {
          Spacer(modifier = Modifier.weight(1f))
        }


        IconButton(onClick = { questionDelete(state.id) }) {
          Icon(
            imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Red
          )
        }
      }

      QuestionContent(
        expanded = expanded,
        state = state,
      )
    }
  }
}

@Composable private fun QuestionContent(
  expanded: Boolean,
  state: QuestionState,
) {
  val onQuestionNameChanged = LocalEditorCallbacks.current
  if (expanded) {
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      value = state.questionText,
      onValueChange = { value ->
        onQuestionNameChanged.onQuestionNameChanged(state.id, value)
      },
      label = {
        Text(text = "Название вопроса")
      }
    )
    Spacer(modifier = Modifier.height(16.dp))
    QuizQuestion(
      state = state,
    )
  }
}