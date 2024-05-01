package ru.kode.base.internship.products.ui.component

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.CardStatus
import ru.kode.base.internship.products.domain.entity.CardType
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun CardItem(modifier: Modifier = Modifier, card: Card) {
  ListItem(
    modifier = modifier,
    colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundSecondary),
    leadingContent = {
      Icon(painter = painterResource(id = R.drawable.input), contentDescription = null)
    },
    headlineContent = {
      Text(
        text = card.title,
        style = AppTheme.typography.body2,
        color = AppTheme.colors.textPrimary
      )
    },
    supportingContent = {
      Text(
        text = when (card.status) {
          CardStatus.ACTIVE -> {
            when (card.type) {
              CardType.PHYSICAL -> stringResource(id = R.string.physical)
              CardType.VIRTUAL -> stringResource(id = R.string.virtual)
            }
          }

          CardStatus.BLOCKED -> {
            stringResource(id = R.string.blocked)
          }
        },
        style = AppTheme.typography.body2,
        color = if (card.status == CardStatus.BLOCKED)
          AppTheme.colors.indicatorContendError
        else
          AppTheme.colors.textTertiary
      )
    },
    trailingContent = {
      IconPlasticCard(number = card.number, logo = card.logo)
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun CardItemLightPreview() {
  AppTheme {
    CardItem(
      card = Card(
        title = "Карта зарплатная",
        status = CardStatus.ACTIVE,
        type = CardType.PHYSICAL,
        icon = R.drawable.ic_master_card,
        logo = R.drawable.master_card_logo,
        number = "1222",
        expiredAt = "08/30",
      )
    )
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun CardItemNightPreview() {
  AppTheme {
    CardItem(
      card = Card(
        title = "Карта зарплатная",
        status = CardStatus.ACTIVE,
        type = CardType.PHYSICAL,
        icon = R.drawable.ic_master_card,
        logo = R.drawable.master_card_logo,
        number = "7721",
        expiredAt = "08/30",
      )
    )
  }
}
