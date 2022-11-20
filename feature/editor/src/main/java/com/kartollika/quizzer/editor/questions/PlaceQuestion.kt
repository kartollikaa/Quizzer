package com.kartollika.quizzer.editor.questions

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kartollika.quizzer.editor.vo.PossibleAnswerVO.Place

@OptIn(ExperimentalPermissionsApi::class)
@Composable fun PlaceQuestion(
  questionId: Int,
  answer: Place,
  onLocationSet: (Int) -> Unit,
  locationEnabled: () -> Boolean
) {
  val permissionKeepTryingAction = remember {
    mutableStateOf<(() -> Unit)?>(null)
  }

  val locationPermission = rememberMultiplePermissionsState(permissions = locationPermissions) {
    if (it.values.all { it }) {
      permissionKeepTryingAction.value?.invoke()
    }
  }

  val locationEnableLauncher =
    rememberLauncherForActivityResult(contract = StartActivityForResult()) {
      if (locationEnabled()) {
        permissionKeepTryingAction.value?.invoke()
      } else {
        permissionKeepTryingAction.value = null
      }
    }

  OutlinedButton(onClick = {
    actionIfEnabledLocation(
      permissionKeepTryingAction,
      permissionsState = locationPermission,
      locationEnabled = locationEnabled,
      locationEnableLauncher = locationEnableLauncher
    ) {
      onLocationSet(questionId)
    }
  }) {
    val text = if (answer.location == null) {
      "Установить локацию, где я сейчас"
    } else {
      val location = answer.location!!
      "Локация: \nLat:${location.latitude}\nLon:${location.longitude}"
    }
    Text(text = text)
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
          state, permissionsState, locationEnabled, locationEnableLauncher, action
        )
      }
    }

    !locationEnabled() -> {
      locationEnableLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
      state.value = {
        actionIfEnabledLocation(
          state, permissionsState, locationEnabled, locationEnableLauncher, action
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