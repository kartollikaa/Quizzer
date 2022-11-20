package com.kartollika.quizzer.player.questions

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kartollika.quizzer.domain.model.Answer
import com.kartollika.quizzer.domain.model.Answer.Place.BeenHere
import com.kartollika.quizzer.domain.model.Answer.Place.BeenHere.BEEN
import com.kartollika.quizzer.domain.model.Answer.Place.BeenHere.NOT_STATED
import com.kartollika.quizzer.domain.model.Answer.Place.BeenHere.REJECTED
import com.kartollika.quizzer.domain.model.Location
import com.kartollika.quizzer.player.QuestionState
import com.kartollika.quizzer.player.QuizPlayerViewModel
import com.kartollika.quizzer.player.vo.PossibleAnswerVO.Place

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PlaceQuestion(
  state: QuestionState,
  viewModel: QuizPlayerViewModel = hiltViewModel(),
  possibleAnswer: Place,
  answer: Answer.Place?,
  modifier: Modifier = Modifier,
  navigateToMap: (Location) -> Unit = { },
  onAnswer: (BeenHere) -> Unit = { },
) {
  val permissionKeepTryingAction = remember {
    mutableStateOf<(() -> Unit)?>(null)
  }

  state.enableCheck = !(answer?.beenHere == NOT_STATED || answer?.beenHere == null)

  val locationPermission = rememberMultiplePermissionsState(permissions = locationPermissions) {
    if (it.values.all { it }) {
      permissionKeepTryingAction.value?.invoke()
    }
  }

  var locationEnabled by remember { mutableStateOf(viewModel.locationEnabled() && locationPermission.allPermissionsGranted) }

  val locationEnableLauncher =
    rememberLauncherForActivityResult(contract = StartActivityForResult()) {
      if (viewModel.locationEnabled()) {
        permissionKeepTryingAction.value?.invoke()
      } else {
        permissionKeepTryingAction.value = null
      }
    }

  if (!locationEnabled) {
    Column(modifier = modifier) {
      Text(text = "Для этого задания потребуется включить геолокацию и дать разрешения на гео")

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Button(
          modifier = Modifier.weight(1f),
          onClick = {
            actionIfEnabledLocation(
              permissionKeepTryingAction,
              locationPermission,
              viewModel::locationEnabled,
              locationEnableLauncher
            ) {
              locationEnabled = true
            }
          }) {
          Text(text = "Дать разрешения")
        }

        Button(
          modifier = Modifier.weight(1f),
          onClick = { onAnswer(REJECTED) }) {
          Text(text = "Отказатья")
        }
      }
    }
    return
  }

  DisposableEffect(Unit) {
    viewModel.startListenLocation()

    onDispose { viewModel.stopListenLocation() }
  }

  val location = possibleAnswer.location
  Column(modifier = modifier) {
    Text(text = "Нужно прийти на локацию сюда!")

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = { navigateToMap(location) }) {
      Text(text = "Lat:${location.latitude} – Lon:${location.longitude}")
    }

    val confirmLocationButtonColors = ButtonDefaults.buttonColors(
      backgroundColor = Color.Green,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      modifier = Modifier
        .height(54.dp)
        .fillMaxWidth(),
      enabled = answer?.isConfirmEnabled ?: false,
      onClick = {
        viewModel.stopListenLocation()
        onAnswer(BEEN)
      },
      colors = confirmLocationButtonColors
    ) {
      Text(text = "Я здесь!")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
      modifier = Modifier
        .height(54.dp)
        .fillMaxWidth(),
      onClick = { onAnswer(REJECTED) },
      colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
    ) {
      Text(text = "Отказаться")
    }
  }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun actionIfEnabledLocation(
  state: MutableState<(() -> Unit)?>,
  permissionsState: MultiplePermissionsState,
  locationEnabled: () -> Boolean,
  locationEnableLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
  action: () -> Unit
) {
  when {
    !permissionsState.allPermissionsGranted -> {
      permissionsState.launchMultiplePermissionRequest()
      state.value = {
        actionIfEnabledLocation(
          state,
          permissionsState,
          locationEnabled,
          locationEnableLauncher,
          action
        )
      }
    }

    !locationEnabled() -> {
      locationEnableLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
      state.value = {
        actionIfEnabledLocation(
          state,
          permissionsState,
          locationEnabled,
          locationEnableLauncher,
          action
        )
      }
    }

    else -> {
      action()
      state.value = null
    }
  }
}

private val locationPermissions = listOf(
  Manifest.permission.ACCESS_COARSE_LOCATION,
  Manifest.permission.ACCESS_FINE_LOCATION
)