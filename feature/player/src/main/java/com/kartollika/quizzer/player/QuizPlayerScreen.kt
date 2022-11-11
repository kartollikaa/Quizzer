@file:OptIn(ExperimentalAnimationApi::class)

package com.kartollika.quizzer.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.player.QuizState.Error
import com.kartollika.quizzer.player.QuizState.Loading
import com.kartollika.quizzer.player.QuizState.Questions
import kotlinx.coroutines.delay

@Composable fun QuizPlayerRoute(
  goBack: () -> Unit, viewModel: QuizPlayerViewModel = hiltViewModel()
) {
  QuizPlayer(viewModel = viewModel, goBack = goBack)
}

@OptIn(ExperimentalAnimationApi::class) @Composable fun QuizPlayer(
  viewModel: QuizPlayerViewModel, goBack: () -> Unit
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


      Scaffold(
        topBar = {
          Column(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
              title = {
                Text(text = questions.quizTitle)
              })

            LinearProgressIndicator(
              color = MaterialTheme.colors.secondary,
              progress = questionState.questionIndex.toFloat() / questionState.totalQuestionsCount,
              modifier = Modifier
                .fillMaxWidth()
            )
          }
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
            onPrevious = { questions.currentQuestionIndex-- },
            onForward = { questions.currentQuestionIndex++ },
            checkAnswer = { questionState.checked = true })
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
  }
}

@Composable fun Question(
  questionState: QuestionState,
  answer: Answer<*>?,
  onAnswer: (Answer<*>) -> Unit,
  modifier: Modifier = Modifier
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