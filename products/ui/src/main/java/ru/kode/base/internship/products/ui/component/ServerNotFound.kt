package ru.kode.base.internship.products.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerNotFound(modifier: Modifier = Modifier, onClose: () -> Unit) {
  Scaffold(modifier = modifier, containerColor = AppTheme.colors.backgroundPrimary, topBar = {
    TopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.colors.backgroundPrimary),
      title = { /*TODO*/ },
      navigationIcon = {
        IconButton(onClick = onClose) {
          Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = AppTheme.colors.textPrimary)
        }
      }
    )
  }) {
    Column(
      modifier = Modifier.padding(it),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Spacer(modifier = Modifier.weight(1f))
      Icon(
        modifier = Modifier.background(AppTheme.colors.contendPrimary, CircleShape),
        painter = painterResource(id = R.drawable.ic_dead_page),
        tint = Color.Unspecified,
        contentDescription = null
      )
      Spacer(modifier = Modifier.height(32.dp))
      Text(
        text = stringResource(R.string.attention),
        style = AppTheme.typography.title,
        color = AppTheme.colors.textPrimary
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        color = AppTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
        style = AppTheme.typography.body1,
        text = stringResource(R.string.server_is_temporarily_unavailable)
      )
      Spacer(modifier = Modifier.weight(1f))
      Button(
        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primaryButton),
        onClick = { /*TODO*/ },
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp)
      ) {
        Text(text = stringResource(R.string.repeat), modifier = Modifier.padding(8.dp))
      }
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = false,
  uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ServerNotFoundNightPreview() {
  AppTheme {
    ServerNotFound(onClose = {})
  }
}

@Preview(
  showBackground = true,
  showSystemUi = false
)
@Composable
private fun ServerNotFoundLightPreview() {
  AppTheme {
    ServerNotFound(onClose = {})
  }
}
