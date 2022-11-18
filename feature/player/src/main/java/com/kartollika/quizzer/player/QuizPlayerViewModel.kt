package com.kartollika.quizzer.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartollika.quizzer.domain.repository.CurrentQuizRepository
import com.kartollika.quizzer.player.QuizState.Error
import com.kartollika.quizzer.player.QuizState.Loading
import com.kartollika.quizzer.player.QuizState.Questions
import com.kartollika.quizzer.player.navigation.quizFileUriArg
import com.kartollika.quizzer.player.vo.QuestionMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QuizPlayerViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val mapper: QuestionMapper,
  private val currentQuizRepository: CurrentQuizRepository
) : ViewModel() {

  private val _uiState: MutableStateFlow<QuizState> = MutableStateFlow(Loading)
  internal val uiState = _uiState.asStateFlow()

  private val filePath: String = checkNotNull(savedStateHandle[quizFileUriArg])

  init {
    currentQuizRepository.openQuiz(filePath)
      .catch { _uiState.tryEmit(Error(it.message ?: "Unexpected error")) }
      .onEach { quiz ->
        val quizState = Questions(
          quizTitle = quiz.title,
          questionsState = quiz.questions.mapIndexed { index, question ->
            QuestionState(
              question = mapper.mapToQuestionVO(question),
              questionIndex = index,
              totalQuestionsCount = quiz.questions.size,
              showPrevious = index > 0,
              showDone = index == quiz.questions.lastIndex
            )
          }
        )
        _uiState.tryEmit(quizState)
      }
      .flowOn(Dispatchers.IO)
      .launchIn(viewModelScope)
  }

  internal fun computeResult(questions: Questions) {
    val answers = questions.questionsState.mapNotNull { it.answer }
    val result = currentQuizRepository.getQuizResults(answers)
    _uiState.value = QuizState.Result(questions.quizTitle, result)
  }
}