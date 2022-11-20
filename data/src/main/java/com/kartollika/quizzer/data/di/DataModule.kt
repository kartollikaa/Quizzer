package com.kartollika.quizzer.data.di

import com.kartollika.quizzer.data.datasource.LocationDataSourceImpl
import com.kartollika.quizzer.data.datasource.QuizFileDataSourceImpl
import com.kartollika.quizzer.data.repository.CurrentQuizRepositoryImpl
import com.kartollika.quizzer.data.repository.QuestDraftRepositoryImpl
import com.kartollika.quizzer.data.repository.QuizEditorRepositoryImpl
import com.kartollika.quizzer.domain.datasource.LocationDataSource
import com.kartollika.quizzer.domain.datasource.QuizFileDataSource
import com.kartollika.quizzer.domain.repository.CurrentQuizRepository
import com.kartollika.quizzer.domain.repository.QuestDraftRepository
import com.kartollika.quizzer.domain.repository.QuizEditorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun bindsQuizFileRepository(quizFileRepository: QuizFileDataSourceImpl): QuizFileDataSource

  @Binds
  fun bindsDraftRepository(draftRepository: QuestDraftRepositoryImpl): QuestDraftRepository

  @Binds
  fun bindsCurrentQuizRepository(currentQuizRepositoryImpl: CurrentQuizRepositoryImpl): CurrentQuizRepository

  @Binds
  fun bindsQuizEditorRepository(quizEditorRepositoryImpl: QuizEditorRepositoryImpl): QuizEditorRepository

  @Binds
  fun bindsLocationDataSource(locationDataSourceImpl: LocationDataSourceImpl): LocationDataSource
}