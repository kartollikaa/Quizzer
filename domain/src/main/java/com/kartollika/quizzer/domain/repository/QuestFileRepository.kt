package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuestFileRepository {
  fun readFile(file: String): Flow<Quiz>
  fun writeToFile(quest: Quiz): Flow<Unit>
}