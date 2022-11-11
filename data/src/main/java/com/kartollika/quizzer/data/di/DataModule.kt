package com.kartollika.quizzer.data.di

import com.kartollika.quizzer.data.repository.CurrentQuizRepositoryImpl
import com.kartollika.quizzer.data.repository.QuestDraftRepositoryImpl
import com.kartollika.quizzer.data.repository.QuizFileRepositoryImpl
import com.kartollika.quizzer.domain.repository.CurrentQuizRepository
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import com.kartollika.quizzer.domain.repository.QuizFileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun bindsQuizFileRepository(quizFileRepository: QuizFileRepositoryImpl): QuizFileRepository

  @Binds
  fun bindsDraftRepository(draftRepository: QuestDraftRepositoryImpl): QuestDraftRepository

  @Binds
  fun bindsCurrentQuizRepository(currentQuizRepositoryImpl: CurrentQuizRepositoryImpl): CurrentQuizRepository
}