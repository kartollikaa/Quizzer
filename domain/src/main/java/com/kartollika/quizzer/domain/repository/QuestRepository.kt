package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quiz
import java.io.File

interface QuestRepository {
  fun generateQuest(quest: Quiz): File
}