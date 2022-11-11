package com.kartollika.quizzer.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import javax.inject.Inject
import javax.inject.Named

class QuestDraftRepositoryImpl @Inject constructor(
  @Named("draft")
  private val sharedPreferences: SharedPreferences,
  private val gson: Gson
) : QuestDraftRepository {
  override suspend fun saveDraft(quest: Quiz) {
    sharedPreferences.edit { gson.toJson(quest) }
  }
}