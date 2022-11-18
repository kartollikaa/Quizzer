package com.kartollika.quizzer.editor.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.max

class BitmapDecoder @Inject constructor(
  @ApplicationContext private val context: Context
) {

  data class DecodeResult(
    val previewBitmap: Bitmap,
    val original: ByteArray
  )

  suspend fun decodeBitmap(uri: Uri): DecodeResult = withContext(Dispatchers.IO) {
    var options = Options()
    options.inJustDecodeBounds = true
    var input: InputStream = context.contentResolver.openInputStream(uri)!!
    BitmapFactory.decodeStream(input, null, options)
    input.close()
    val originalSize = max(options.outHeight, options.outWidth)
    val ratio = if (originalSize > 176) originalSize / 176.0 else 1.0

    options = Options()
    options.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
    input = context.contentResolver.openInputStream(uri)!!
    val previewBitmap: Bitmap = BitmapFactory.decodeStream(input, null, options)!!
    input.close()

    options = Options()
    input = context.contentResolver.openInputStream(uri)!!
    options.inSampleSize = 2
    val bitmapFull = BitmapFactory.decodeStream(input, null, options)!!
    input.close()

    val baos = ByteArrayOutputStream()
    bitmapFull.compress(JPEG, 60, baos)

    bitmapFull.recycle()

    return@withContext DecodeResult(previewBitmap, baos.toByteArray())
  }

  private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
    val k = Integer.highestOneBit(floor(ratio).toInt())
    return if (k == 0) 1 else k
  }

    /*val options = BitmapFactory.Options()
//    options.inJustDecodeBounds = true
//    options.inScaled = true
//    options.outWidth = 200
//    options.outHeight = 200

    val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)!!
    return@withContext bitmap*/
    /*return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
      val baos = ByteArrayOutputStream()
      bitmap.compress(JPEG, 80, baos)
      val baossize: Int = baos.size() / 1024
      val uploadbaos: ByteArray = baos.toByteArray()
      val lengthbmp = uploadbaos.size
      val size = lengthbmp / 1024

      return@withContext BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size())
    } else {
      MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
  }*/
}