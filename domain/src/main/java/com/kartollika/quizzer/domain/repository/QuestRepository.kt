package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quest
import java.io.File

interface QuestRepository {
  fun generateQuest(quest: Quest): File
}