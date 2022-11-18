package com.kartollika.quizzer.editor.vo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EditorMapperModule {

  @Provides
  @Singleton
  fun provideQuestionEditorMapper() = QuestionEditorMapper()
}