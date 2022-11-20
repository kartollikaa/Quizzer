package com.kartollika.quizzer.editor

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartollika.quizzer.domain.datasource.LocationDataSource
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import com.kartollika.quizzer.domain.repository.QuizEditorRepository
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType.Input
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType.Location
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType.SingleChoice
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType.Slides
import com.kartollika.quizzer.editor.bitmap.BitmapDecoder
import com.kartollika.quizzer.editor.bitmap.BitmapDecoder.DecodeResult
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.SingleChoice.OptionVO
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides.Slide
import com.kartollika.quizzer.editor.vo.QuestionEditorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizEditorViewModel @Inject constructor(
  private val quizDraftRepository: QuestDraftRepository,
  private val quizEditorRepository: QuizEditorRepository,
  private val questionMapper: QuestionEditorMapper,
  private val bitmapDecoder: BitmapDecoder,
  private val locationDataSource: LocationDataSource
) : ViewModel() {

  private val _uiState: MutableStateFlow<QuizEditorState> = MutableStateFlow(QuizEditorState())
  internal val uiState = _uiState.asStateFlow()

  private val _toastMessage = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
  val toastMessage = _toastMessage.asSharedFlow()

  fun addNewQuestion() {
    val id = quizEditorRepository.getNextQuestionId()
    _uiState.value.questions = uiState.value.questions + QuestionState(id = id)
  }

  fun generateQuiz() {
    uiState.value.quizGenerating = true

    val state = uiState.value

    flowOf(state)
      .map { questionMapper.quizStateToModel(state) }
      .flatMapLatest { quizEditorRepository.generateQuiz(it) }
      .flowOn(Dispatchers.Default)
      .onEach { file -> _uiState.value.fileToShare = file }
      .flowOn(Dispatchers.IO)
      .catch { exception -> exception.message?.let { message -> _toastMessage.tryEmit(message) } }
      .launchIn(viewModelScope)
  }

  fun onQuestionTypeSelected(questionId: Int, questionType: QuestionType) {
    val question = getQuestionById(questionId) ?: return
    question.answer = when (questionType) {
      Slides -> PossibleAnswerVO.Slides(Slide(getNextId()))
      Input -> PossibleAnswerVO.Input()
      SingleChoice -> {
        PossibleAnswerVO.SingleChoice(OptionVO(id = getNextId()))
      }
      Location -> PossibleAnswerVO.Place()
    }
  }

  fun onQuestionDelete(questionId: Int) {
    val questionToDelete = getQuestionById(questionId) ?: return
    _uiState.value.questions = uiState.value.questions - questionToDelete
  }

  fun setQuestionName(questionId: Int, name: String) {
    getQuestionById(questionId)?.questionText = name
  }

  fun addOption(questionId: Int) {
    val singleChoice = getQuestionById(questionId)?.answer as PossibleAnswerVO.SingleChoice
    (singleChoice).options = singleChoice.options + OptionVO(id = getNextId())
  }

  fun addSlide(questionId: Int) {
    val slides = getQuestionById(questionId)?.answer as PossibleAnswerVO.Slides
    slides.slides = slides.slides.toMutableList().apply {
      add(Slide(getNextId()))
    }
  }

  fun deleteSlide(questionId: Int, slideId: Int) {
    val slides = getQuestionById(questionId)?.answer as PossibleAnswerVO.Slides
    slides.slides = slides.slides.toMutableList().apply {
      removeAll { it.id == slideId }
    }
  }

  fun onAddPictureOnSlide(slide: Slide, uri: Uri) {
    viewModelScope.launch {
      val decodeResult: DecodeResult = bitmapDecoder.decodeBitmap(uri)
      slide.pictures = slide.pictures.toMutableList().apply {
        add(decodeResult)
      }
    }
  }

  fun onLocationSet(questionId: Int) {
    viewModelScope.launch {
      val location = locationDataSource.getLastKnownLocation().first()
      getQuestionById(questionId)?.answer = PossibleAnswerVO.Place(location)
    }
  }

  fun deleteOption(questionId: Int, optionId: Int) {
    val singleChoice = getQuestionById(questionId)?.answer as PossibleAnswerVO.SingleChoice
    singleChoice.options = singleChoice.options.toMutableList().apply {
      removeAll { it.id == optionId }
    }
  }

  fun endLinking(questionId: Int) {
    val startLinking = uiState.value.isLinkingQuestions ?: return

    val singleChoice =
      getQuestionById(startLinking.questionId)?.answer as PossibleAnswerVO.SingleChoice
    singleChoice.options.find { it.id == startLinking.optionId }?.linkedQuestionId = questionId
    uiState.value.isLinkingQuestions = null
  }

  fun cancelLinking() {
    uiState.value.isLinkingQuestions = null
  }

  fun startLinking(questionId: Int, optionId: Int) {
    uiState.value.isLinkingQuestions = LinkingStart(questionId, optionId)
  }

  fun locationEnabled() = locationDataSource.locationEnabled()

  private fun getQuestionById(questionId: Int) =
    _uiState.value.questions.find { it.id == questionId }

  private fun getNextId() = quizEditorRepository.getNextId()

  enum class QuestionType {
    Slides,
    Input,
    SingleChoice,
    Location
  }

  data class LinkingStart(
    val questionId: Int,
    val optionId: Int
  )
}

val LocalEditorCallbacks = compositionLocalOf { EditorCallbacks() }

data class EditorCallbacks(
  val onQuestionNameChanged: (Int, String) -> Unit = { _, _ -> },
  val onAddOption: (Int) -> Unit = {},
  val onQuestionDelete: (Int) -> Unit = {},
  val onQuestionTypeSelected: (Int, QuestionType) -> Unit = { _, _ -> },
  val onAddSlide: (Int) -> Unit = {},
  val onAddPictureOnSlide: (Slide, Uri) -> Unit = { _, _ -> },
  val onLocationSet: (Int) -> Unit = {},
  val onOptionDeleted: (Int, Int) -> Unit = { _, _ -> },
  val startLinking: (Int, Int) -> Unit = { _, _ -> },
  val endLinking: (Int) -> Unit = { },
  val cancelLinking: () -> Unit = {},
  val deleteSlide: (Int, Int) -> Unit = { _, _ -> },
  val locationEnabled: () -> Boolean = { false }
)