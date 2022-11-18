package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuestDraftRepository {
  suspend fun saveDraft(quest: Quiz): Flow<Unit>
}