package ru.kode.base.internship.ui.core.uikit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun SuccessSnackbar(
  modifier: Modifier = Modifier,
  message: String,
  onClose: () -> Unit,
) {
  Snackbar(
    action = {
      IconButton(onClick = onClose) {
        Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = AppTheme.colors.textButton)
      }
    },
    modifier = modifier,
    shape = RoundedCornerShape(13.dp),
    backgroundColor = AppTheme.colors.indicatorContendSuccess,
  ) {
    Text(
      modifier = Modifier.padding(vertical = 10.dp),
      text = message,
      style = AppTheme.typography.caption1,
      color = AppTheme.colors.textButton,
    )
  }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
internal fun SuccessSnackbarPreview() {
  AppTheme {
    Column {
      SuccessSnackbar(
        modifier = Modifier.padding(16.dp),
        message = "Message",
        onClose = {}
      )
      SuccessSnackbar(
        modifier = Modifier.padding(16.dp),
        message = "Long Message Long Message Long Message  Long Message",
        onClose = {}
      )
    }
  }
}