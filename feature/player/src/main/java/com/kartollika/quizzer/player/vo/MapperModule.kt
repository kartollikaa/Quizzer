package com.kartollika.quizzer.player.vo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

  @Provides
  @Singleton
  fun provideQuestionMapper() = QuestionMapper()
}