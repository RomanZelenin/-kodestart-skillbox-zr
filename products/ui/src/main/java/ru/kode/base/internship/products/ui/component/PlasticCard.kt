package ru.kode.base.internship.products.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import format
import ru.kode.base.internship.products.domain.entity.CardStatus
import ru.kode.base.internship.products.domain.entity.CardType
import ru.kode.base.internship.products.domain.entity.CurrencySign
import ru.kode.base.internship.products.domain.entity.Money
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme
import ru.kode.base.internship.ui.core.uikit.theme.Red2
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PlasticCard(
  modifier: Modifier = Modifier,
  @DrawableRes logo: Int,
  title: String,
  cardType: CardType,
  money: Money,
  number: String,
  status: CardStatus,
  expiredAt: String,
) {
  Surface(
    modifier = modifier
      .height(160.dp)
      .width(272.dp),
    shape = RoundedCornerShape(8.dp),
    shadowElevation = 8.dp,
    color = Color.Transparent
  )
  {
    Box {
      Image(
        painter = painterResource(id = R.drawable.visa_card_bg),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
      )
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Start,
          modifier = Modifier.fillMaxWidth()
        ) {
          Image(
            painter = painterResource(id = logo),
            contentDescription = null,
            modifier = Modifier.padding(top = 20.dp, start = 16.dp)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column(modifier = Modifier.padding(top = 18.dp)) {
            Text(
              modifier = Modifier.width(150.dp),
              text = title,
              color = Color.White,
              style = AppTheme.typography.body2,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )

            Text(
              text = when (cardType) {
                CardType.PHYSICAL -> stringResource(id = R.string.physical)
                CardType.VIRTUAL -> stringResource(id = R.string.virtual)
              }.lowercase(),
              color = AppTheme.colors.textTertiary,
              style = AppTheme.typography.caption1
            )
          }
          Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Image(
              modifier = Modifier
                .padding(end = 24.dp, top = 16.dp),
              painter = painterResource(id = R.drawable.ic_paypass),
              contentDescription = null
            )
          }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (status == CardStatus.BLOCKED) {
          Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(id = R.string.blocked),
            color = Red2,
            style = AppTheme.typography.bodySemibold
          )
        } else {
          Text(
            modifier = Modifier.padding(start = 16.dp),
            text = money.format(),
            color = Color.White,
            style = AppTheme.typography.bodySemibold,
            maxLines = 1,
            overflow = TextOverflow.Clip
          )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row(
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "${stringResource(R.string.four_hidden_symbols)}  ${number.takeLast(4)}",
            color = Color.White,
            style = AppTheme.typography.caption1
          )
          Text(text = expiredAt.formatToDate(), color = Color.White, style = AppTheme.typography.caption1)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun CreditCardPreview() {
  AppTheme {
    PlasticCard(
      logo = R.drawable.master_card_logo,
      title = "Дополнительная карта",
      cardType = CardType.VIRTUAL,
      money = Money("125555.3458", CurrencySign.RUB),
      number = "7788",
      expiredAt = "05/22",
      status = CardStatus.ACTIVE
    )
  }
}

private const val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private fun String.formatToDate(): String {
  val date = SimpleDateFormat(dateTimePattern, Locale.getDefault()).parse(this)
  return SimpleDateFormat("MM/yy", Locale.getDefault()).format(date!!)
}