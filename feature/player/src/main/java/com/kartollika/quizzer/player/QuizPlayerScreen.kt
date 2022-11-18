@file:OptIn(ExperimentalAnimationApi::class)

package com.kartollika.quizzer.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.player.QuizState.Error
import com.kartollika.quizzer.player.QuizState.Loading
import com.kartollika.quizzer.player.QuizState.Questions
import com.kartollika.quizzer.player.QuizState.Result
import kotlinx.coroutines.delay

@Composable fun QuizPlayerRoute(
  onBack: () -> Unit,
  viewModel: QuizPlayerViewModel = hiltViewModel()
) {
  QuizPlayer(
    viewModel = viewModel,
    goBack = onBack,
  )
}

@OptIn(ExperimentalAnimationApi::class) @Composable fun QuizPlayer(
  viewModel: QuizPlayerViewModel,
  goBack: () -> Unit
) {
  val state: QuizState by viewModel.uiState.collectAsState()

  when (state) {
    Loading -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    }
    is Questions -> {
      val questions: Questions = state as Questions

      val questionState: QuestionState = remember(questions.currentQuestionIndex) {
        questions.questionsState[questions.currentQuestionIndex]
      }

      if (questions.goBack) {
        goBack()
      }

      Scaffold(
        topBar = {
          QuizTopAppBar(
            title = questions.quizTitle,
            questionIndex = questionState.questionIndex,
            totalQuestionsCount = questionState.totalQuestionsCount,
            onBack = goBack
          )
        },
        content = { innerPadding ->
          AnimatedContent(targetState = questionState, transitionSpec = {
            val animationSpec: TweenSpec<IntOffset> = tween(150)
            val direction = if (targetState.questionIndex > initialState.questionIndex) {
              // Going forwards in the survey: Set the initial offset to start
              // at the size of the content so it slides in from right to left, and
              // slides out from the left of the screen to -fullWidth
              SlideDirection.Left
            } else {
              // Going back to the previous question in the set, we do the same
              // transition as above, but with different offsets - the inverse of
              // above, negative fullWidth to enter, and fullWidth to exit.
              SlideDirection.Right
            }
            slideIntoContainer(
              towards = direction, animationSpec = animationSpec
            ) with slideOutOfContainer(
              towards = direction, animationSpec = animationSpec
            )
          }) { targetState ->
            Question(
              questionState = targetState,
              answer = targetState.answer,
              onAnswer = {
                targetState.answer = it
                targetState.enableNext = true
              },
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            )
          }
        }, bottomBar = {
          QuestNavigatorController(
            state = questionState,
            onPrevious = { viewModel.showPrevious() },
            onForward = { viewModel.showNext() },
            checkAnswer = { questionState.checked = true },
            onDone = { viewModel.computeResult(questions) }
          )
        })
    }
    is Error -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = (state as Error).cause)
        LaunchedEffect(key1 = Unit) {
          delay(1500L)
          goBack()
        }
      }
    }
    is Result -> {
      val state = state as Result
      Surface(modifier = Modifier.fillMaxSize()) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = "Итоги теста ${state.quizTitle}")
          Text(text = "Правильных ответов: ${state.quizResult.correctAnswers}/${state.quizResult.totalQuestions}")
        }
      }
    }
  }
}

@Composable fun QuizTopAppBar(
  title: String,
  questionIndex: Int,
  totalQuestionsCount: Int,
  modifier: Modifier = Modifier,
  onBack: () -> Unit
) {
  Column(modifier = modifier) {
    TopAppBar(
      modifier = Modifier.fillMaxWidth(),
      title = {
        Text(text = title, color = MaterialTheme.colors.onSurface)
      },
      actions = {
        IconButton(
          onClick = onBack,
          modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .size(32.dp)
        ) {
          Icon(
            Icons.Filled.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colors.onSurface
          )
        }
      },
      backgroundColor = Color.Transparent
    )

    val animatedProgress: Float by animateFloatAsState(
      targetValue = (questionIndex + 1).toFloat() / totalQuestionsCount.toFloat(),
      animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    LinearProgressIndicator(
      progress = animatedProgress,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      color = MaterialTheme.colors.primary.copy(alpha = 0.8f),
    )
  }
}

@Composable
internal fun Question(
  questionState: QuestionState,
  answer: Answer<*>?,
  onAnswer: (Answer<*>) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    QuestionContent(
      questionState = questionState,
      answer = answer,
      onAnswer = onAnswer,
      modifier = Modifier.matchParentSize(),
    )
  }
}