package ru.kode.base.internship.products.ui.carddetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.ui.core.uikit.screen.AppScreen
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CardDetailsScreen(cardId: String, viewModel: CardDetailsViewModel = daggerViewModel()) = AppScreen(
  viewModel = viewModel,
  intents = rememberViewIntents()
) { state, intents ->
  Scaffold(containerColor = AppTheme.colors.backgroundSecondary,
    topBar = {
      CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = AppTheme.colors.backgroundSecondary,
          scrolledContainerColor = AppTheme.colors.backgroundSecondary
        ),
        title = {
          Text(
            text = stringResource(R.string.cards), color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.subtitle
          )
        },
        navigationIcon = {
          IconButton(onClick = { intents.navigateOnBack() }) {
            Icon(
              painter = painterResource(id = R.drawable.ic_left),
              contentDescription = null,
              tint = AppTheme.colors.textPrimary
            )
          }
        })
    }) {
    Column(
      modifier = Modifier
        .padding(it)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      val pagerState = rememberPagerState(pageCount = { 2 })
      HorizontalPager(state = pagerState) { page ->
        Card(
          colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.visa_card_big), contentDescription = null)
          }
        }
      }
      Row(
        Modifier
          .wrapContentHeight()
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        repeat(pagerState.pageCount) { iteration ->
          val color = if (pagerState.currentPage == iteration) AppTheme.colors.contendAccentPrimary else Color.LightGray
          Box(
            modifier = Modifier
              .padding(6.dp)
              .clip(CircleShape)
              .background(color)
              .size(6.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        IconButton(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(color = AppTheme.colors.contendSecondary),
          onClick = { /*TODO*/ }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_history),
            contentDescription = null,
            tint = AppTheme.colors.textPrimary
          )
        }
        IconButton(modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(color = AppTheme.colors.buttonSecondary),
          onClick = { /*TODO*/ }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_card),
            tint = AppTheme.colors.contendAccentPrimary,
            contentDescription = null
          )
        }
        IconButton(modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(color = AppTheme.colors.contendSecondary),
          onClick = { /*TODO*/ }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_main_product),
            contentDescription = null,
            tint = AppTheme.colors.textPrimary
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
      cardActions.forEachIndexed { index, pair ->
        ListItem(
          colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { },
          leadingContent = {
            Icon(
              painter = painterResource(id = pair.first),
              contentDescription = null
            )
          },
          headlineContent = { Text(color = AppTheme.colors.textPrimary, text = stringResource(id = pair.second)) })
        if (index < cardActions.size - 1)
          HorizontalDivider(
            color = AppTheme.colors.contendSecondary,
            modifier = Modifier.offset(56.dp)
          )
      }
    }
  }
}

private val cardActions = listOf(
  R.drawable.ic_edit_card_name to R.string.rename_card,
  R.drawable.ic_account_details to R.string.account_details,
  R.drawable.ic_card_info to R.string.card_info,
  R.drawable.ic_issue_card to R.string.issue_card,
  R.drawable.ic_block_card to R.string.block_card,
)

@Preview(showBackground = true)
@Composable
private fun CardDetailsScreenPreview() {
  AppTheme {
    CardDetailsScreen("1")
  }
}