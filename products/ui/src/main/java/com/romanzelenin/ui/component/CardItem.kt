package com.romanzelenin.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.romanzelenin.ui.R
import com.romanzelenin.ui.home.CardStatus
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun CardItem(modifier: Modifier = Modifier, title: String, status: CardStatus, icon: Int) {
  ListItem(
    modifier = modifier,
    colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundSecondary),
    leadingContent = {
      Icon(painter = painterResource(id = R.drawable.input), contentDescription = null)
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
        text = status.title,
        style = AppTheme.typography.body2,
        color = if (status == CardStatus.BLOCKED)
          AppTheme.colors.indicatorContendError
        else
          AppTheme.colors.textTertiary
      )
    },
    trailingContent = {
      Image(painter = painterResource(id = icon), contentDescription = null)
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun CardItemLightPreview() {
  AppTheme {
    CardItem(
      title = "Карта зарплатная",
      status = CardStatus.PHYSICAL,
      icon = R.drawable.ic_master_card
    )
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun CardItemNightPreview() {
  AppTheme {
    CardItem(
      title = "Карта зарплатная",
      status = CardStatus.PHYSICAL,
      icon = R.drawable.ic_master_card
    )
  }
}
