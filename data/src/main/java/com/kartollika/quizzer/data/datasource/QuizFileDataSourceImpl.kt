package com.kartollika.quizzer.data.datasource

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.kartollika.quizzer.domain.datasource.QuizFileDataSource
import com.kartollika.quizzer.domain.model.Quiz
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.util.UUID
import javax.inject.Inject

class QuizFileDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val gson: Gson
): QuizFileDataSource {

  override suspend fun readFile(file: String): Quiz = withContext(Dispatchers.IO) {
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
      return@withContext gson.fromJson(String(byteArray, Charsets.UTF_8), Quiz::class.java)
    }
  }

  override suspend fun writeToFile(quiz: Quiz): File = withContext(Dispatchers.IO) {
    val quizJson = gson.toJson(quiz)
    val file = File(context.filesDir, buildQuizFileName(quiz))
    val fileStream = FileWriter(file)

    fileStream.use { outStream ->
      outStream.write(quizJson)
    }
    return@withContext file
  }

  private fun buildQuizFileName(quiz: Quiz): String {
    val title = quiz.title.takeIf { it.isNotEmpty() } ?: UUID.randomUUID().toString()
    return "${title}.quiz"
  }
}