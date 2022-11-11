package com.kartollika.quizzer.data.di

import com.kartollika.quizzer.data.repository.QuestDraftRepositoryImpl
import com.kartollika.quizzer.data.repository.QuestFileRepositoryImpl
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import com.kartollika.quizzer.domain.repository.QuestFileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun bindsTopicRepository(topicsRepository: QuestFileRepositoryImpl): QuestFileRepository

  @Binds
  fun bindsDraftRepository(draftRepository: QuestDraftRepositoryImpl): QuestDraftRepository
}