package com.romanzelenin.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romanzelenin.ui.home.CurrencySigns
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun DepositItem(
  modifier: Modifier = Modifier,
  title: String,
  amount: String,
  sign: CurrencySigns,
  rate: String,
  date: String
) {
  ListItem(
    modifier = modifier,
    colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundSecondary),
    leadingContent = {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(color = AppTheme.colors.contendSecondary, shape = CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = sign.code,
          color = AppTheme.colors.textPrimary,
          fontSize = 26.sp,
          fontWeight = FontWeight.Light
        )
      }
    },
    headlineContent = {
      Text(
        text = title,
        style = AppTheme.typography.body2,
        color = AppTheme.colors.textPrimary
      )
    },
    supportingContent = {
      Text(
        text = "$amount ${sign.code}",
        style = AppTheme.typography.body2,
        color = AppTheme.colors.contendAccentPrimary
      )
    },
    trailingContent = {
      Column(verticalArrangement = Arrangement.SpaceBetween) {
        Text(text = "Ставка $rate%", color = AppTheme.colors.textTertiary, style = AppTheme.typography.caption2)
        Spacer(modifier = Modifier.height(7.dp))
        Text(text = "до $date", color = AppTheme.colors.textTertiary, style = AppTheme.typography.caption2)
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun DepositItemLightPreview() {
  AppTheme {
    DepositItem(
      title = "Мой вклад",
      amount = "1 515 000,78",
      sign = CurrencySigns.RUB,
      rate = "7.65",
      date = "31.08.2024"
    )
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun DepositItemNightPreview() {
  AppTheme {
    DepositItem(
      title = "Мой вклад",
      amount = "1 515 000,78",
      sign = CurrencySigns.RUB,
      rate = "7.65",
      date = "31.08.2024"
    )
  }
}
