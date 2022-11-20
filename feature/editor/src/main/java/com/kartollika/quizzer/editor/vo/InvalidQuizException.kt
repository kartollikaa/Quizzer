package com.kartollika.quizzer.editor.vo

data class QuizInvalidMessage(
  val stringRes: Int,
  val questionId: Int
)

sealed class InvalidQuizException(open val invalidMessage: QuizInvalidMessage) : Throwable() {
  data class InputEmptyException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
  data class ChoiceOptionEmptyException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
  data class OptionNotSelectedException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
  data class NoSlidesException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
  data class SlideEmptyException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
  data class LocationEmptyException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
  data class QuestionNotSelectedException(override val invalidMessage: QuizInvalidMessage) : InvalidQuizException(invalidMessage)
}