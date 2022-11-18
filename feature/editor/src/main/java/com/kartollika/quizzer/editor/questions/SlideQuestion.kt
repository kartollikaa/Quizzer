package com.kartollika.quizzer.editor.questions

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kartollika.quizzer.editor.QuestionState
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Slides.Slide

@SuppressLint("NewApi")
@Composable internal fun SlideQuestion(
  state: QuestionState,
  answer: Slides,
  onAddSlide: (Int) -> Unit,
  addPictureOnSlide: (Slide, Uri) -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    answer.slides.forEach { slide: Slide ->
      val horizontalScrollState = rememberScrollState()

      val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = GetContent(),
      ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        addPictureOnSlide(slide, uri)
      }

      Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "Слайд:")
        OutlinedTextField(
          value = slide.text,
          onValueChange = { value ->
            slide.text = value
          },
          label = {
            Text(text = "Описание слайда")
          })

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .horizontalScroll(horizontalScrollState),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          slide.pictures.forEach { picture ->
            Image(
              bitmap = picture.previewBitmap.asImageBitmap(),
              contentDescription = null,
              modifier = Modifier.size(64.dp),
              contentScale = ContentScale.Crop
            )
          }

          IconButton(modifier = Modifier.background(color = Color.LightGray, shape = CircleShape),
            onClick = {
              pickPhotoLauncher.launch("image/*")
            }) {
            Icon(
              imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White
            )
          }
        }
      }
    }

    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(CenterHorizontally), onClick = { onAddSlide(state.id) }
    ) {
      Text(text = "Добавить слайд")
    }
  }
}