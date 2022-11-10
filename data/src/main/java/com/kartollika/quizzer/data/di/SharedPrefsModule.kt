package com.kartollika.quizzer.data.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefsModule {

  @Provides
  @Singleton
  @Named(DRAFT_QUEST_NAME)
  fun provideDraftQuestSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
    return context.getSharedPreferences(DRAFT_QUEST_NAME, MODE_PRIVATE)
  }

  private const val DRAFT_QUEST_NAME = "draft"
}