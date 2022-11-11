package com.kartollika.quizzer.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartollika.quizzer.domain.repository.QuestFileRepository
import com.kartollika.quizzer.player.QuizState.Error
import com.kartollika.quizzer.player.QuizState.Loading
import com.kartollika.quizzer.player.navigation.quizFileUriArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QuizPlayerViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle, private val questFileRepository: QuestFileRepository
) : ViewModel() {

  private val _uiState: MutableStateFlow<QuizState> = MutableStateFlow(Loading)
  val uiState = _uiState.asStateFlow()

  private val filePath: String = checkNotNull(savedStateHandle[quizFileUriArg])

  init {
    questFileRepository.readFile(filePath)
      .catch { _uiState.tryEmit(Error(it.message ?: "Unexpected error")) }
      .onEach { quiz ->
        val quizState = QuizState.Questions(
          quizTitle = quiz.title,
          questionsState = quiz.questions.mapIndexed { index, question ->
            QuestionState(
              question = question,
              questionIndex = index,
              totalQuestionsCount = quiz.questions.size,
              showPrevious = index > 0,
              showDone = index == quiz.questions.lastIndex
            )
          }
        )
        _uiState.tryEmit(quizState)
      }
      .launchIn(viewModelScope)
  }

  fun checkAnswer(index: Int) {
//    val questState = uiState.value as QuestState
//    val progress = questState.progress.toMutableMap()
//    progress[step] = answer
//
//    _uiState.tryEmit(questState.copy(progress = progress as LinkedHashMap<Step, Answer?>))
  }
}