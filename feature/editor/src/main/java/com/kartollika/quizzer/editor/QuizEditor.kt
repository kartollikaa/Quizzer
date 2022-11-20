package com.kartollika.quizzer.editor

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.kartollika.quizzer.editor.R.string

@OptIn(ExperimentalFoundationApi::class)
@Composable internal fun QuizEditor(
  viewModel: QuizEditorViewModel = hiltViewModel(),
  state: QuizEditorState,
  modifier: Modifier = Modifier,
  addNewQuestion: () -> Unit = {},
  generateQuiz: () -> Unit = {},
  goBack: () -> Unit = {}
) {
  val endLinking = LocalEditorCallbacks.current.endLinking
  val cancelLinking = LocalEditorCallbacks.current.cancelLinking

  val scrollableState = rememberLazyListState()

  val linking = state.isLinkingQuestions

  val context = LocalContext.current
  LaunchedEffect(Unit) {
    viewModel.toastMessage
      .collect { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
      }
  }

  if (linking != null) {
    AlertDialog(
      onDismissRequest = { cancelLinking() },
      title = { Text(text = "Куда ведет ответ?") },
      text = {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
          items(state.questions.filter { it.id > linking.questionId }) { question ->
            Box(modifier = Modifier
              .clickable { endLinking(question.id) }
              .padding(16.dp)
              .fillMaxWidth(),
              contentAlignment = CenterStart
            ) {
              Text(
                text = "${question.id} – ${question.questionText}"
              )
            }
          }
        }
      },
      buttons = {}
    )
  }

  if (state.showSetQuizTitleDialog) {
    AlertDialog(
      properties = DialogProperties(dismissOnClickOutside = false),
      onDismissRequest = goBack,
      title = {
        Text(text = "Название квеста")
      },
      text = {
        OutlinedTextField(
          value = state.quizTitle,
          onValueChange = {
            state.quizTitle = it
          }
        )
      },
      confirmButton = {
        TextButton(
          enabled = state.quizTitle.isNotEmpty(),
          onClick = { state.showSetQuizTitleDialog = false }
        ) {
          Text(text = "Создать")
        }
      }
    )
  }

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
          index = question.id,
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
      Icon(imageVector = Icons.Default.Check, contentDescription = stringResource(string.accessibility_generate_quest))
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
        CircleBadge(index)

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
            imageVector = Icons.Default.Delete, contentDescription = stringResource(string.accessibility_delete_question), tint = Color.Red
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