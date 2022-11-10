package com.kartollika.quizzer.domain.repository

import com.kartollika.quizzer.domain.model.Quest

interface QuestDraftRepository {
  suspend fun saveDraft(quest: Quest)
}