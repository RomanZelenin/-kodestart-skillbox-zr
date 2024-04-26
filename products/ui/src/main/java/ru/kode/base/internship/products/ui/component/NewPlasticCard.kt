package ru.kode.base.internship.products.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun NewPlasticCard(modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier
      .height(160.dp)
      .width(272.dp),
    shape = RoundedCornerShape(8.dp),
    shadowElevation = 8.dp,
    color = AppTheme.colors.contendAccentPrimary
  )
  {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
      IconButton(onClick = { /*TODO*/ }) {
        Image(painter = painterResource(id = R.drawable.circle_plus), contentDescription = null)
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = stringResource(R.string.new_card),
        color = AppTheme.colors.textButton,
        style = AppTheme.typography.subtitle
      )
    }
  }
}

@Preview
@Composable
private fun NewPlasticCardPreview() {
  AppTheme {
    NewPlasticCard()
  }
}