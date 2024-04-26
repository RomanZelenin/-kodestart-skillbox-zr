package ru.kode.base.internship.products.ui.carddetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.products.ui.component.PlasticCard
import ru.kode.base.internship.ui.core.uikit.component.ErrorSnackbar
import ru.kode.base.internship.ui.core.uikit.component.SuccessSnackbar
import ru.kode.base.internship.ui.core.uikit.screen.AppScreen
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CardDetailsScreen(
  modifier: Modifier = Modifier,
  accountId: Account.Id,
  cardId: Card.Id,
  viewModel: CardDetailsViewModel = daggerViewModel(),
) =
  AppScreen(
    viewModel = viewModel,
    intents = rememberViewIntents()
  ) { state, intents ->
    LaunchedEffect(key1 = Unit) {
      intents.loadCardInfo(cardId)
      intents.loadAccount(accountId)
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
      modifier = modifier,
      containerColor = AppTheme.colors.backgroundSecondary,
      snackbarHost = {
        SnackbarHost(
          hostState = snackbarHostState,
          snackbar = { _ ->
            if (state.errorMessage != null) {
              ErrorSnackbar(modifier = Modifier.padding(16.dp), message = state.errorMessage)
            } else if (state.successMessage != null) {
              SuccessSnackbar(modifier = Modifier.padding(16.dp), message = state.successMessage)
            }
          })
      },
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
      if (state.errorMessage != null) {
        LaunchedEffect(state.errorMessage) {
          snackbarHostState.showSnackbar(message = state.errorMessage)
          intents.dismissError()
        }
      }
      if (state.isShowRenameCardDialog) {
        var cardName by remember { mutableStateOf("") }
        AlertDialog(
          containerColor = AppTheme.colors.backgroundPrimary,
          title = { Text(text = stringResource(R.string.rename), color = AppTheme.colors.textPrimary) },
          text = {
            Column {
              Text(text = stringResource(R.string.input_new_name), color = AppTheme.colors.textPrimary)
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedTextField(
                textStyle = TextStyle.Default.copy(color = AppTheme.colors.textPrimary),
                placeholder = { Text(text = state.currentCard?.title ?: "") },
                value = cardName,
                onValueChange = { cardName = it })
            }
          },
          onDismissRequest = { intents.showRenameCardDialog(false) },
          confirmButton = {
            TextButton(onClick = {
              intents.saveCardName(cardName)
            })
            {
              Text(text = stringResource(R.string.save), color = AppTheme.colors.contendAccentPrimary)
            }
          },
          dismissButton = {
            TextButton(
              onClick = { intents.showRenameCardDialog(false) })
            {
              Text(text = stringResource(R.string.cancel), color = AppTheme.colors.contendAccentPrimary)
            }
          }
        )
      }
      Column(
        modifier = Modifier
          .padding(it)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        state.account?.let { account ->
          state.currentCard?.let { card ->
            val pagerState = rememberPagerState(
              pageCount = { account.attachedCards.size },
              // initialPage = account.attachedCards.indexOf(cardId)
            )
            HorizontalPager(state = pagerState) { page ->
              //intents.loadCardInfo(account.attachedCards[page])
              Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PlasticCard(
                  logo = card.logo,
                  title = card.title,
                  cardType = card.type,
                  money = account.money,
                  number = card.number,
                  date = card.expiryDate,
                  status = card.status
                )
              }
            }
            Row(
              Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
              horizontalArrangement = Arrangement.Center
            ) {
              repeat(pagerState.pageCount) { iteration ->
                val color =
                  if (pagerState.currentPage == iteration) AppTheme.colors.contendAccentPrimary else Color.LightGray
                Box(
                  modifier = Modifier
                    .padding(6.dp, top = 24.dp, bottom = 8.dp)
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
                onClick = { /*TODO*/ }
              ) {
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
            cardActions.forEachIndexed { index, item ->
              ListItem(
                colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundPrimary),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { item.action(intents, state.currentCard) },
                leadingContent = {
                  Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null
                  )
                },
                headlineContent = { Text(color = AppTheme.colors.textPrimary, text = stringResource(id = item.title)) })
              if (index < cardActions.size - 1)
                HorizontalDivider(
                  color = AppTheme.colors.contendSecondary,
                  modifier = Modifier.offset(56.dp)
                )
            }
          }
        }
      }
    }
  }

@Immutable
private data class CardAction(
  @DrawableRes val icon: Int,
  @StringRes val title: Int,
  val action: (CardDetailsIntents, Card) -> Unit,
)

private val cardActions = listOf(
  CardAction(
    icon = R.drawable.ic_edit_card_name, title = R.string.rename_card,
    action = { cardDetailsIntents, _ ->
      cardDetailsIntents.showRenameCardDialog(true)
    }),
  CardAction(
    icon = R.drawable.ic_account_details,
    title = R.string.account_details,
    action = { cardDetailsIntents, card -> }
  ),
  CardAction(icon = R.drawable.ic_card_info, title = R.string.card_info, action = { cardDetailsIntents, card -> }),
  CardAction(icon = R.drawable.ic_issue_card, title = R.string.issue_card, action = { cardDetailsIntents, card -> }),
  CardAction(icon = R.drawable.ic_block_card, title = R.string.block_card, action = { cardDetailsIntents, card -> }),
)

@Preview(showBackground = true)
@Composable
private fun CardDetailsScreenPreview() {
  AppTheme {
    CardDetailsScreen(modifier = Modifier, accountId = Account.Id("1"), cardId = Card.Id("1"))
  }
}
