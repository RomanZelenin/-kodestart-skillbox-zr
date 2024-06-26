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
fun ErrorSnackbar(
  modifier: Modifier = Modifier,
  message: String,
  onClose: () -> Unit,
) {
  Snackbar(
    modifier = modifier,
    action = {
      IconButton(onClick = onClose) {
        Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = AppTheme.colors.textButton)
      }
    },
    shape = RoundedCornerShape(13.dp),
    backgroundColor = AppTheme.colors.indicatorContendError,
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
internal fun ErrorSnackbarPreview() {
  AppTheme {
    Column {
      ErrorSnackbar(
        modifier = Modifier.padding(16.dp),
        message = "Message",
        onClose = {}
      )
      ErrorSnackbar(
        modifier = Modifier.padding(16.dp),
        message = "Long Message Long Message Long Message  Long Message",
        onClose = {}
      )
    }
  }
}
