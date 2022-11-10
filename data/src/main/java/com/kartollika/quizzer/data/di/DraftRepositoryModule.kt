package com.kartollika.quizzer.data.di

import com.kartollika.quizzer.data.repository.QuestDraftRepositoryImpl
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DraftRepositoryModule {

  @Binds
  abstract fun provideDraftRepository(draftRepository: QuestDraftRepositoryImpl): QuestDraftRepository
}