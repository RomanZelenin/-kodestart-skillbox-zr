package ru.kode.base.internship.products.ui.carddetails

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import ru.kode.base.core.compose.OnBackPressedHandler
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.core.domain.entity.LceState
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
import ru.kode.base.internship.ui.core.uikit.theme.Blue2


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    LaunchedEffect(Unit) {
      intents.setCurrentCard(cardId)
      intents.loadCards(accountId)
    }
    Box(
      modifier = Modifier
        .statusBarsPadding()
        .navigationBarsPadding()
        .imePadding(),
    ) {
      if (state.isShowCardRenaming) {
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

        when (state.loadStateCards) {
          LceState.Content -> {
            val pagerState = rememberPagerState(
              pageCount = { state.cards.size + 1 },
              initialPage = maxOf(0, state.cards.indexOfFirst { card -> card.id == cardId })
            )
            if (pagerState.currentPage < state.cards.size)
              intents.setCurrentCard(state.cards[pagerState.currentPage].id)
            else
              intents.setCurrentCard(Card.Id("none"))
            HorizontalPager(state = pagerState, contentPadding = PaddingValues(top = 8.dp)) { page ->
              Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (page < pagerState.pageCount - 1) {
                  PlasticCard(
                    logo = state.cards[page].logo,
                    title = state.cards[page].title,
                    cardType = state.cards[page].type,
                    money = state.money!!,
                    number = state.cards[page].number,
                    expiredAt = state.cards[page].expiredAt,
                    status = state.cards[page].status
                  )
                } else {
                  NewPlasticCard()
                }
              }
            }
            BulletPageIndicators(pagerState = pagerState)

          }

          is LceState.Error -> {}
          LceState.Loading -> {}
          LceState.None -> {}
        }

        if (state.currentCard != Card.Id("none")) {
          Spacer(modifier = Modifier.height(24.dp))
          TabBar()
          Spacer(modifier = Modifier.height(24.dp))
          CardActionsTab(intents = intents)
        }
      }
      if (state.isShowNotification) {
        Snackbar(
          modifier = Modifier
            .align(Alignment.TopCenter),
          message = state.errorMessage?.name() ?: stringResource(R.string.card_successfully_renamed),
          onDismiss = intents.dismissNotification,
          isError = state.errorMessage != null
        )
      }

    }
  }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.BulletPageIndicators(pagerState: PagerState) {
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
            .padding(8.dp, top = 24.dp, bottom = 8.dp)
            .clip(CircleShape)
            .background(color)
            .size(6.dp)
        )
      } else {
        Icon(
          modifier = Modifier
            .padding(8.dp, top = 24.dp, bottom = 8.dp),
          painter = painterResource(id = R.drawable.ic_rub),
          contentDescription = null,
          tint = color
        )
      }
    }
  }
}

private val cardTabs = listOf(
  TabBarItem(icon = R.drawable.ic_history, title = R.string.operations_history) { },
  TabBarItem(icon = R.drawable.ic_card, title = R.string.card_actions, selected = true) { },
  TabBarItem(icon = R.drawable.ic_main_product, title = R.string.payments) { },
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
    action = { _, _ -> }
  ),
  CardActionItem(icon = R.drawable.ic_card_info, title = R.string.card_info, action = { _, _ -> }),
  CardActionItem(
    icon = R.drawable.ic_issue_card,
    title = R.string.issue_card,
    action = { _, _ -> }),
  CardActionItem(
    icon = R.drawable.ic_block_card,
    title = R.string.block_card,
    action = { _, _ -> }),
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

@Composable
private fun ActionCardItem(modifier: Modifier = Modifier, @DrawableRes icon: Int, @StringRes title: Int) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(color = AppTheme.colors.backgroundPrimary)
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun ActionCardItemDarkPreview() {
  AppTheme {
    ActionCardItem(icon = R.drawable.ic_edit_card_name, title = R.string.rename_card)
  }
}

@Composable
private fun RenameCardDialog(onDismiss: () -> Unit = {}, onConfirm: (String) -> Unit = {}) {
  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(Unit) {
    delay(100)
    focusRequester.requestFocus()
  }
  var cardName by remember { mutableStateOf("") }
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = AppTheme.colors.backgroundSecondary
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
          Text(
            text = stringResource(id = R.string.rename),
            style = AppTheme.typography.subtitle,
            color = AppTheme.colors.textPrimary
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = stringResource(id = R.string.input_new_name),
            style = AppTheme.typography.caption1,
            color = AppTheme.colors.textPrimary
          )
          Spacer(modifier = Modifier.height(18.dp))
          BasicTextField(
            cursorBrush = SolidColor(Blue2),
            singleLine = true,
            modifier = Modifier
              .padding(6.dp)
              .height(24.dp)
              .border(0.5.dp, color = AppTheme.colors.contendTertiary, shape = RoundedCornerShape(6.dp))
              .fillMaxWidth()
              .focusRequester(focusRequester),
            value = cardName,
            onValueChange = { cardName = it },
            textStyle = TextStyle.Default.copy(
              color = AppTheme.colors.textPrimary,
              fontSize = AppTheme.typography.caption1.fontSize,
              fontFamily = AppTheme.typography.caption1.fontFamily,
              fontWeight = AppTheme.typography.caption1.fontWeight,
              fontStyle = AppTheme.typography.caption1.fontStyle
            ),
            decorationBox = { innerTextField ->
              Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                innerTextField()
              }
            }
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = AppTheme.colors.contendTertiary, thickness = 0.5.dp)
        Row(modifier = Modifier.height(46.dp)) {
          TextButton(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight(),
            onClick = { onDismiss() },
            shape = RectangleShape
          ) {
            Text(text = stringResource(id = R.string.cancel), color = Blue2, style = AppTheme.typography.bodyMedium)
          }
          VerticalDivider(color = AppTheme.colors.contendTertiary, thickness = 0.5.dp)
          TextButton(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight(),
            onClick = { onConfirm(cardName) },
            shape = RectangleShape
          ) {
            Text(text = stringResource(id = R.string.save), color = Blue2, style = AppTheme.typography.bodyMedium)
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun RenameCardDialogPreview() {
  AppTheme {
    RenameCardDialog()
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun RenameCardDialogDarkPreview() {
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
        ErrorSnackbar(modifier = Modifier.padding(16.dp), message = snackBarData.visuals.message, onClose = onDismiss)
      else
        SuccessSnackbar(modifier = Modifier.padding(16.dp), message = snackBarData.visuals.message, onClose = onDismiss)
    }
  )
}

@Composable
private fun CardDetailsErrorMessage.name(): String {
  return when (this) {
    CardDetailsErrorMessage.ValidationError.EmptyCardName -> {
      stringResource(R.string.validation_error_empty_card_name)
    }

    CardDetailsErrorMessage.SyncCardName -> {
      stringResource(R.string.rename_card_error_check_internet_connection)
    }
  }
}
