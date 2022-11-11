package com.kartollika.quizzer.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.kartollika.quizzer.domain.model.Quiz
import com.kartollika.quizzer.domain.repository.QuizFileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class QuizFileRepositoryImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val gson: Gson
): QuizFileRepository {

  override fun readFile(file: String): Flow<Quiz> = flow {
    val fileUri = Uri.parse(file)
    if (File(fileUri.path!!).extension != "quiz") {
      throw IllegalArgumentException("Illegal file format. Expected *.quiz file")
    }

    context.contentResolver.openInputStream(fileUri).use { inputStream ->
      val outputStream = ByteArrayOutputStream()
      val bufferSize = 1024
      val buffer = ByteArray(bufferSize)
      var len: Int
      len = inputStream!!.read(buffer)
      while (len != -1) {
        outputStream.write(buffer, 0, len)
        len = inputStream.read(buffer)
      }
      val byteArray = outputStream.toByteArray()
      val quest = gson.fromJson(String(byteArray, Charsets.UTF_8), Quiz::class.java)
      emit(quest)
    }
  }

  override fun writeToFile(quest: Quiz): Flow<Unit> = flow {
    val questJson = gson.toJson(quest)
    emit(Unit)
  }
}