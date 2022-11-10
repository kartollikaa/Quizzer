package com.kartollika.quizzer.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kartollika.quizzer.data.adapters.StepTypeDeserializer
import com.kartollika.quizzer.data.adapters.StepTypeSerializer
import com.kartollika.quizzer.domain.model.StepType
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
      .registerTypeAdapter(StepType::class.java, StepTypeSerializer())
      .registerTypeAdapter(StepType::class.java, StepTypeDeserializer())
      .create()
  }
}