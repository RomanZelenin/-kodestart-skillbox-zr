package ru.kode.base.internship.products.ui.carddetails

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.OutlinedTextField
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
import ru.kode.base.core.compose.OnBackPressedHandler
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.products.ui.carddetails.entity.CardDetailsErrorMessage
import ru.kode.base.internship.products.ui.component.NewPlasticCard
import ru.kode.base.internship.products.ui.component.PlasticCard
import ru.kode.base.internship.ui.core.uikit.component.ErrorSnackbar
import ru.kode.base.internship.ui.core.uikit.component.SuccessSnackbar
import ru.kode.base.internship.ui.core.uikit.screen.AppScreen
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme


@Composable
fun CardDetailsScreen(
  accountId: Account.Id,
  cardId: Card.Id,
  viewModel: CardDetailsViewModel = daggerViewModel(),
) =
  AppScreen(
    viewModel = viewModel,
    intents = rememberViewIntents()
  ) { state, intents ->
    OnBackPressedHandler(onBack = intents.navigateOnBack)
    LaunchedEffect(key1 = Unit) {
      intents.loadCardInfo(cardId)
      intents.loadAccount(accountId)
    }
    CardDetailsWithoutScaffold(state = state, intents = intents)
  }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CardDetailsWithoutScaffold(
  state: CardDetailsViewState = CardDetailsViewState(),
  intents: CardDetailsIntents = CardDetailsIntents(),
) {
  Box(
    modifier = Modifier
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding(),
  ) {
    if (state.isShowRenameCardDialog) {
      RenameCardDialog(
        onDismiss = { intents.showRenameCardDialog(false) },
        onConfirm = { intents.saveCardName(it) }
      )
    }
    Column(
      modifier = Modifier
        .background(color = AppTheme.colors.backgroundSecondary)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = AppTheme.colors.backgroundSecondary,
          scrolledContainerColor = AppTheme.colors.backgroundSecondary
        ),
        title = {
          Text(
            text = stringResource(R.string.cards),
            color = AppTheme.colors.textPrimary,
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
      state.account?.let { account ->
        state.currentCard?.let { card ->
          val pagerState = rememberPagerState(
            pageCount = { account.attachedCards.size + 1 },
            //initialPage = account.attachedCards.indexOf(card.id)
          )
          HorizontalPager(state = pagerState, contentPadding = PaddingValues(top = 8.dp)) { page ->
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
              if (page < pagerState.pageCount - 1) {
                PlasticCard(
                  logo = card.logo,
                  title = card.title,
                  cardType = card.type,
                  money = account.money,
                  number = card.number,
                  date = card.expiryDate,
                  status = card.status
                )
              } else {
                NewPlasticCard()
              }
            }
          }
          Row(
            Modifier
              .wrapContentHeight()
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            repeat(pagerState.pageCount) { iteration ->
              val color =
                if (pagerState.currentPage == iteration) AppTheme.colors.contendAccentPrimary else Color.LightGray

              if (iteration < pagerState.pageCount - 1) {
                Box(
                  modifier = Modifier
                    .padding(6.dp, top = 24.dp, bottom = 8.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(6.dp)
                )
              } else {
                Icon(
                  modifier = Modifier
                    .padding(6.dp, top = 24.dp, bottom = 8.dp),
                  painter = painterResource(id = R.drawable.ic_rub),
                  contentDescription = null,
                  tint = color
                )
              }
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(24.dp))
      TabBar()
      Spacer(modifier = Modifier.height(24.dp))
      CardActionsTab(intents = intents, card = state.currentCard)
    }
    if (state.isShowSnackbar) {
      Snackbar(
        modifier = Modifier.align(Alignment.TopCenter),
        message = state.errorMessage?.name() ?: stringResource(R.string.card_successfully_renamed),
        onDismiss = intents.dismissSnackbar,
        isError = state.errorMessage != null
      )
    }

  }
}

private val cardTabs = listOf(
  TabBarItem(icon = R.drawable.ic_history, title = R.string.operations_history) { },
  TabBarItem(icon = R.drawable.ic_card, title = R.string.operations_history, selected = true) { },
  TabBarItem(icon = R.drawable.ic_main_product, title = R.string.operations_history) { },
)

@Composable
private fun TabBar() {
  Row(
    horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
      .fillMaxWidth()
  ) {
    cardTabs.forEach {
      IconButton(
        modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(color = if (!it.selected) AppTheme.colors.contendSecondary else AppTheme.colors.buttonSecondary),
        onClick = it.action
      ) {
        Icon(
          painter = painterResource(id = it.icon),
          contentDescription = stringResource(id = it.title),
          tint = if (!it.selected) AppTheme.colors.textPrimary else AppTheme.colors.contendSecondary
        )
      }
    }
  }
}

private val cardActions = listOf(
  CardActionItem(
    icon = R.drawable.ic_edit_card_name, title = R.string.rename_card,
    action = { cardDetailsIntents, _ ->
      cardDetailsIntents.showRenameCardDialog(true)
    }),
  CardActionItem(
    icon = R.drawable.ic_account_details,
    title = R.string.account_details,
    action = { cardDetailsIntents, card -> }
  ),
  CardActionItem(icon = R.drawable.ic_card_info, title = R.string.card_info, action = { cardDetailsIntents, card -> }),
  CardActionItem(
    icon = R.drawable.ic_issue_card,
    title = R.string.issue_card,
    action = { cardDetailsIntents, card -> }),
  CardActionItem(
    icon = R.drawable.ic_block_card,
    title = R.string.block_card,
    action = { cardDetailsIntents, card -> }),
)

@Composable
private fun CardActionsTab(intents: CardDetailsIntents = CardDetailsIntents(), card: Card? = null) {
  cardActions.forEachIndexed { index, item ->
    ActionCardItem(
      modifier = Modifier
        .background(AppTheme.colors.backgroundPrimary)
        .clickable { item.action(intents, card) },
      icon = item.icon,
      title = item.title
    )
    if (index < cardActions.size - 1)
      HorizontalDivider(
        color = AppTheme.colors.contendSecondary,
        modifier = Modifier.offset(56.dp)
      )
  }
}

@Immutable
private data class TabBarItem(
  @DrawableRes val icon: Int,
  @StringRes val title: Int,
  val selected: Boolean = false,
  val action: () -> Unit,
)

@Immutable
private data class CardActionItem(
  @DrawableRes val icon: Int,
  @StringRes val title: Int,
  val action: (CardDetailsIntents, Card?) -> Unit,
)

@Preview(showBackground = true)
@Composable
private fun CardDetailsWithoutScaffoldLightPreview() {
  AppTheme {
    CardDetailsWithoutScaffold()
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun CardDetailsWithoutScaffoldNightPreview() {
  AppTheme {
    CardDetailsWithoutScaffold()
  }
}

@Composable
private fun ActionCardItem(modifier: Modifier = Modifier, @DrawableRes icon: Int, @StringRes title: Int) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      modifier = Modifier.size(28.dp),
      painter = painterResource(id = icon),
      contentDescription = null,
      tint = AppTheme.colors.contendTertiary
    )
    Spacer(modifier = Modifier.width(16.dp))
    Text(
      text = stringResource(id = title),
      color = AppTheme.colors.textSecondary,
      style = AppTheme.typography.body2
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ActionCardItemPreview() {
  AppTheme {
    ActionCardItem(icon = R.drawable.ic_edit_card_name, title = R.string.rename_card)
  }
}

@Composable
private fun RenameCardDialog(onDismiss: () -> Unit = {}, onConfirm: (String) -> Unit = {}) {
  var cardName by remember { mutableStateOf("") }
  AlertDialog(
    containerColor = AppTheme.colors.backgroundPrimary,
    title = {
      Text(
        text = stringResource(R.string.rename),
        color = AppTheme.colors.textPrimary,
        //  style = AppTheme.typography.subtitle
      )
    },
    text = {
      Column {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          label = {
            Text(
              text = stringResource(R.string.input_new_name),
              color = AppTheme.colors.textPrimary,
              // style = AppTheme.typography.caption1
            )
          },
          textStyle = TextStyle.Default.copy(color = AppTheme.colors.textPrimary),
          value = cardName,
          onValueChange = { cardName = it },
          maxLines = 1,
        )
      }
    },
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(onClick = { onConfirm(cardName) })
      {
        Text(
          text = stringResource(R.string.save),
          color = AppTheme.colors.contendAccentPrimary,
          //  style = AppTheme.typography.subtitle
        )
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss)
      {
        Text(
          text = stringResource(R.string.cancel),
          color = AppTheme.colors.contendAccentPrimary,
          // style = AppTheme.typography.subtitle
        )
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun RenameCardDialogPreview() {
  AppTheme {
    RenameCardDialog()
  }
}

@Composable
private fun Snackbar(
  modifier: Modifier = Modifier,
  message: String,
  onDismiss: () -> Unit,
  isError: Boolean,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  LaunchedEffect(message) {
    snackbarHostState.showSnackbar(message = message)
    onDismiss()
  }

  SnackbarHost(
    modifier = modifier,
    hostState = snackbarHostState,
    snackbar = { snackBarData ->
      if (isError)
        ErrorSnackbar(modifier = Modifier.padding(16.dp), message = snackBarData.visuals.message)
      else
        SuccessSnackbar(modifier = Modifier.padding(16.dp), message = snackBarData.visuals.message)
    }
  )
}

@Composable
private fun CardDetailsErrorMessage.name(): String {
  return when (this) {
    CardDetailsErrorMessage.ValidationError.EmptyCardName -> {
      stringResource(R.string.validation_error_empty_card_name)
    }
  }
}