package com.kartollika.quizzer.data.repository

import com.kartollika.quizzer.domain.datasource.QuizFileDataSource
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.repository.QuizEditorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class QuizEditorRepositoryImpl @Inject constructor(
  private val quizFileDataSource: QuizFileDataSource
) : QuizEditorRepository {

  private var lastId = 0

  override fun getNextId(): Int = lastId++

  override fun generateQuiz(quiz: Quiz): Flow<File> = flow {
    emit(quizFileDataSource.writeToFile(quiz))
  }
}