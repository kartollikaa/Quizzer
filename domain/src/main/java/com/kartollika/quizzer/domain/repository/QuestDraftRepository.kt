package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quiz

interface QuestDraftRepository {
  suspend fun saveDraft(quest: Quiz)
}