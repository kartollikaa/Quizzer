package com.kartollika.quizzer.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kartollika.quizzer.data.adapters.PossibleAnswerDeserializer
import com.kartollika.quizzer.data.adapters.PossibleAnswerSerializer
import com.kartollika.quizzer.domain.model.PossibleAnswer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GsonModule {

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder()
      .registerTypeAdapter(PossibleAnswer::class.java, PossibleAnswerSerializer())
      .registerTypeAdapter(PossibleAnswer::class.java, PossibleAnswerDeserializer())
      .create()
  }
}