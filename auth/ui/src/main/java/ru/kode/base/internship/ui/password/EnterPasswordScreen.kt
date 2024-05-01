package ru.kode.base.internship.ui.password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.kode.base.core.compose.OnBackPressedHandler
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.auth.ui.R
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.ui.component.TextField
import ru.kode.base.internship.ui.core.uikit.component.ErrorSnackbar
import ru.kode.base.internship.ui.core.uikit.component.PrimaryButton
import ru.kode.base.internship.ui.core.uikit.screen.AppScreen
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme
import ru.kode.base.internship.ui.password.entity.EnterPasswordErrorMessage

@Composable
fun EnterPasswordScreen(viewModel: EnterPasswordViewModel = daggerViewModel()) = AppScreen(
  viewModel = viewModel,
  intents = rememberViewIntents()
) { state, intents ->
  OnBackPressedHandler(onBack = intents.navigateOnBack)
  Box(
    modifier = Modifier
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding(),
  ) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
      modifier = Modifier.padding(horizontal = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.height(56.dp))
      Image(
        painter = painterResource(id = R.drawable.img_kode_logo_90x40),
        contentDescription = "KODE logo"
      )
      Spacer(modifier = Modifier.weight(2f))
      val visualTransformation = if (state.isPasswordProtected) {
        PasswordVisualTransformation()
      } else {
        VisualTransformation.None
      }
      TextField(
        inputValue = state.enteredPassword,
        placeholder = stringResource(id = R.string.user_identification_password),
        onValueChange = intents.changeText,
        leadingIcon = {
          Icon(
            modifier = Modifier.padding(start = 16.dp),
            painter = painterResource(id = R.drawable.ic_lock_24),
            contentDescription = "phone icon",
            tint = AppTheme.colors.contendAccentPrimary
          )
        },
        trailingIcon = {
          PasswordFieldTrailingIcon(
            isLoginInProgress = state.loginLceState == LceState.Loading,
            isPasswordProtected = state.isPasswordProtected,
            onTogglePasswordVisibility = intents.togglePasswordVisibility,
          )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        keyboardActions = KeyboardActions { keyboardController?.hide() },
        visualTransformation = visualTransformation
      )
      Spacer(modifier = Modifier.weight(weight = 5f))
      PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.user_identification_enter),
        onClick = {
          keyboardController?.hide()
          intents.login()
        },
        enabled = state.loginLceState != LceState.Loading
      )
      Spacer(modifier = Modifier.height(height = 24.dp))
    }
    Snackbar(
      modifier = Modifier.align(Alignment.BottomCenter),
      message = state.errorMessage?.name(),
      onDismiss = intents.dismissError,
    )
  }
}

@Composable
private fun Snackbar(
  message: String?,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val snackbarHostState = remember { SnackbarHostState() }

  if (message != null) {
    LaunchedEffect(message) {
      snackbarHostState.showSnackbar(message = message)
      onDismiss()
    }
  }

  SnackbarHost(
    modifier = modifier,
    hostState = snackbarHostState,
    snackbar = { snackBarData ->
      ErrorSnackbar(modifier = Modifier.padding(16.dp), message = snackBarData.message, onClose = onDismiss)
    }
  )
}

@Composable
private fun PasswordFieldTrailingIcon(
  isLoginInProgress: Boolean,
  isPasswordProtected: Boolean,
  onTogglePasswordVisibility: () -> Unit,
) {
  if (isLoginInProgress) {
    CircularProgressIndicator(
      modifier = Modifier
        .padding(end = 16.dp)
        .size(20.dp),
      color = AppTheme.colors.contendAccentPrimary,
      strokeWidth = 1.5.dp
    )
  } else {
    IconButton(
      modifier = Modifier.padding(start = 8.dp, end = 16.dp),
      onClick = onTogglePasswordVisibility,
    ) {
      Icon(
        painter = painterResource(
          id = if (isPasswordProtected) R.drawable.ic_crossed_eye_24 else R.drawable.ic_eye_24
        ),
        contentDescription = "phone icon",
        tint = AppTheme.colors.textTertiary
      )
    }
  }
}

@Composable
private fun EnterPasswordErrorMessage.name(): String {
  return when (this) {
    EnterPasswordErrorMessage.LoginError -> stringResource(id = R.string.error_something_went_wrong_title)
  }
}
