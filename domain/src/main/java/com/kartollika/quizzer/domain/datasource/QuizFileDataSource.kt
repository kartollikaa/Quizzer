package com.kartollika.quizzer.domain.datasource

import com.kartollika.quizzer.domain.model.Quiz
import java.io.File

interface QuizFileDataSource {
  suspend fun readFile(file: String): Quiz
  suspend fun writeToFile(quiz: Quiz): File
}