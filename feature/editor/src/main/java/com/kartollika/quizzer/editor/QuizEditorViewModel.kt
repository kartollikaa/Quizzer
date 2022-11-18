package com.kartollika.quizzer.editor

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartollika.quizzer.domain.model.Question
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import com.kartollika.quizzer.domain.repository.QuizEditorRepository
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType
import com.kartollika.quizzer.editor.QuizEditorViewModel.QuestionType.Input
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizEditorViewModel @Inject constructor(
  private val quizDraftRepository: QuestDraftRepository,
  private val quizEditorRepository: QuizEditorRepository,
  private val questionMapper: QuestionEditorMapper,
  private val bitmapDecoder: BitmapDecoder
) : ViewModel() {

  private val _uiState: MutableStateFlow<QuizEditorState> = MutableStateFlow(QuizEditorState())
  internal val uiState = _uiState.asStateFlow()

  fun setQuizTitle(title: String) {
    _uiState.update {
      it.copy(quizTitle = title)
    }
  }

  fun addNewQuestion() {
    val id = quizEditorRepository.getNextId()
    _uiState.value.questions = uiState.value.questions + QuestionState(id = id)
  }

  fun generateQuiz() {
    uiState.value.quizGenerating = true

    viewModelScope.launch(Dispatchers.IO) {
      val state = uiState.value

      val quiz = Quiz(
        title = state.quizTitle,
        questions = state.questions.map {
          Question(it.id, it.questionText, questionMapper.answerToModel(it.answer))
        }
      )

      quizEditorRepository.generateQuiz(quiz)
        .onEach { file -> _uiState.value.fileToShare = file }
        .collect()
    }
  }

  fun onQuestionTypeSelected(questionId: Int, questionType: QuestionType) {
    val question = getQuestionById(questionId) ?: return
    question.answer = when (questionType) {
      Slides -> PossibleAnswerVO.Slides()
      Input -> PossibleAnswerVO.Input()
      SingleChoice -> {
        PossibleAnswerVO.SingleChoice(OptionVO(id = quizEditorRepository.getNextId()))
      }
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
    (singleChoice).options = singleChoice.options + OptionVO(id = quizEditorRepository.getNextId())
  }

  fun addSlide(questionId: Int) {
    val slides = getQuestionById(questionId)?.answer as PossibleAnswerVO.Slides
    slides.slides = slides.slides.toMutableList().apply {
      add(Slide())
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

  private fun getQuestionById(questionId: Int) =
    _uiState.value.questions.find { it.id == questionId }

  enum class QuestionType {
    Slides,
    Input,
    SingleChoice
  }
}

val LocalEditorCallbacks = compositionLocalOf { EditorCallbacks() }

data class EditorCallbacks(
  val onQuestionNameChanged: (Int, String) -> Unit = { _, _ -> },
  val onAddOption: (Int) -> Unit = {},
  val onQuestionDelete: (Int) -> Unit = {},
  val onQuestionTypeSelected: (Int, QuestionType) -> Unit = { _, _ -> },
  val onAddSlide: (Int) -> Unit = {},
  val onAddPictureOnSlide: (Slide, Uri) -> Unit = { _, _ -> }
)