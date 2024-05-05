package ru.kode.base.internship.products.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@Composable
fun IconPlasticCard(modifier: Modifier = Modifier, number: String, @DrawableRes logo: Int) {
  Surface(
    modifier = modifier
      .height(28.dp)
      .width(40.dp),
    shape = RoundedCornerShape(2.dp),
    tonalElevation = 8.dp,
    color = Color.Transparent
  ) {
    Box {
      Image(
        painter = painterResource(id = R.drawable.visa_card_bg),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
      )
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(2.dp), horizontalAlignment = Alignment.End
      ) {
        Text(text = number.takeLast(4), color = AppTheme.colors.textButton, style = AppTheme.typography.caption2)
        Image(painter = painterResource(id = logo), contentDescription = null)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun IconPlasticCardPreview() {
  AppTheme {
    IconPlasticCard(number = "7789", logo = R.drawable.visa_card_logo)
  }
}