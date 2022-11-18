package com.kartollika.quizzer.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class QuestDraftRepositoryImpl @Inject constructor(
  @Named("draft")
  private val sharedPreferences: SharedPreferences,
  private val gson: Gson
) : QuestDraftRepository {

  override suspend fun saveDraft(quest: Quiz): Flow<Unit> = flow {
    sharedPreferences.edit { gson.toJson(quest) }
    emit(Unit)
  }
}