package ru.kode.base.internship.products.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.products.ui.home.CurrencySigns
import ru.kode.base.internship.products.ui.home.Money
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun AccountItem(
  modifier: Modifier = Modifier,
  title: String,
  money: Money,
  isExpanded: Boolean = false,
  onClick: () -> Unit,
) {
  ListItem(
    modifier = modifier,
    colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundSecondary),
    leadingContent = {
      Text(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(
            color = AppTheme.colors.contendSecondary,
            shape = CircleShape
          )
          .offset(y = 4.dp),
        text = money.sign.code,
        color = AppTheme.colors.textPrimary,
        fontSize = 26.sp,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Center
      )
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
        text = money.format(),
        style = AppTheme.typography.body2,
        color = AppTheme.colors.contendAccentPrimary
      )
    },
    trailingContent = {
      FilledIconButton(
        modifier = Modifier
          .width(40.dp)
          .height(28.dp),
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = AppTheme.colors.contendSecondary)
      ) {
        Icon(
          painter = painterResource(id = if (!isExpanded) R.drawable.ic_chevron_down else R.drawable.ic_chevron_up),
          contentDescription = null,
          tint = AppTheme.colors.contendTertiary
        )
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun AccountItemLightPreview() {
  AppTheme {
    AccountItem(
      title = stringResource(id = R.string.current_account),
      money = Money(amount = "457 334,00", sign = CurrencySigns.RUB),
      onClick = {}
    )
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun AccountItemNightPreview() {
  AppTheme {
    AccountItem(
      title = stringResource(id = R.string.current_account),
      money = Money(amount = "457 334,00", sign = CurrencySigns.RUB),
      onClick = {}
    )
  }
}
