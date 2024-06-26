package ru.kode.base.internship.products.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.ui.R
import ru.kode.base.internship.products.ui.component.AccountItem
import ru.kode.base.internship.products.ui.component.CardItem
import ru.kode.base.internship.products.ui.component.DepositItem
import ru.kode.base.internship.ui.core.uikit.screen.AppScreen
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProductsHomeScreen(viewModel: ProductsHomeViewModel = daggerViewModel()) =
  AppScreen(
    viewModel = viewModel,
    intents = rememberViewIntents()
  ) { state, intents ->
    val appBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pullRefreshState = rememberPullRefreshState(
      refreshing = state.isRefreshing,
      onRefresh = { intents.refresh() }
    )
    Box(
      modifier = Modifier
        .statusBarsPadding()
        .navigationBarsPadding()
        .imePadding(),
    ) {
      Column(
        modifier = Modifier
          .pullRefresh(pullRefreshState)
          .background(color = AppTheme.colors.backgroundPrimary)
      ) {
        MediumTopAppBar(
          colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = AppTheme.colors.backgroundPrimary,
            scrolledContainerColor = AppTheme.colors.backgroundPrimary
          ),
          title = {
            Text(
              text = stringResource(R.string.main),
              color = AppTheme.colors.textPrimary,
              style = AppTheme.typography.largeTitle
            )
          },
          scrollBehavior = appBarBehaviour
        )
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .nestedScroll(appBarBehaviour.nestedScrollConnection)
        ) {
          accounts(state = state, intents = intents, shimmer = shimmerInstance)
          item { Spacer(modifier = Modifier.height(16.dp)) }
          deposits(state = state, intents = intents, shimmer = shimmerInstance)
        }
      }
      PullRefreshIndicator(
        state.isRefreshing,
        pullRefreshState,
        Modifier.align(Alignment.TopCenter),
        backgroundColor = AppTheme.colors.backgroundPrimary
      )
    }
  }

private fun LazyListScope.accounts(state: ProductsHomeViewState, intents: ProductsHomeIntents, shimmer: Shimmer) {
  item {
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundSecondary)
    ) {
      Text(
        modifier = Modifier.padding(16.dp),
        text = stringResource(id = R.string.accounts),
        color = AppTheme.colors.textTertiary,
        style = AppTheme.typography.bodySemibold
      )
      when (state.accountsLceState) {
        is LceState.Error -> {
          ErrorLoadingPartContent(onClickRefresh = { intents.loadAccounts() })
        }

        LceState.Loading -> {
          ListShimmerContents(size = 3, shimmerInstance = shimmer)
        }

        LceState.Content -> {
          for (account in state.accounts) {
            val isExpanded = state.expandedAccountIds.contains(account.id)
            val currentDp = if (isExpanded) (72.dp * account.attachedCards.size) else 0.dp
            val animateDpExpanded = animateDpAsState(targetValue = currentDp, label = "")

            AccountItem(
              title = account.title,
              money = account.money,
              isExpanded = isExpanded
            ) {
              intents.expandAccount(account.id)
            }
            Column(modifier = Modifier.height(animateDpExpanded.value)) {
              account.attachedCards.forEachIndexed { index, card ->
                CardItem(
                  modifier = Modifier.clickable { intents.showCardDetails(AccountIdWithCardId(accountId = account.id, card.id)) },
                  card = ru.kode.base.internship.products.domain.entity.Card(
                    title = card.title,
                    status = card.status,
                    icon = card.icon,
                    type = card.type,
                    expiredAt = card.expiredAt,
                    logo = card.logo,
                    number = card.number
                  )
                )
                if (index < account.attachedCards.size - 1)
                  HorizontalDivider(
                    color = AppTheme.colors.contendSecondary,
                    modifier = Modifier.offset(56.dp)
                  )
              }
            }
          }
        }

        LceState.None -> {}
      }
    }
  }
}

private fun LazyListScope.deposits(state: ProductsHomeViewState, intents: ProductsHomeIntents, shimmer: Shimmer) {
  item {
    Card(
      modifier = Modifier.fillMaxSize(),
      colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundSecondary)
    ) {
      Text(
        modifier = Modifier.padding(16.dp),
        text = stringResource(id = R.string.deposits),
        color = AppTheme.colors.textTertiary,
        style = AppTheme.typography.bodySemibold
      )
      when (state.depositsLceState) {
        LceState.Loading -> {
          ListShimmerContents(size = 4, shimmerInstance = shimmer)
        }

        LceState.Content -> {
          state.deposits.forEachIndexed { index, deposit ->
            DepositItem(
              modifier = Modifier.clickable { },
              title = deposit.title,
              money = deposit.money,
              rate = deposit.term.rate,
              date = deposit.term.date
            )
            if (index < state.deposits.size - 1)
              HorizontalDivider(color = AppTheme.colors.contendSecondary, modifier = Modifier.offset(70.dp))
          }
        }

        is LceState.Error -> {
          ErrorLoadingPartContent(onClickRefresh = { intents.loadDeposits() })
        }

        LceState.None -> {}
      }
    }
  }
}

@Composable
private fun ListShimmerContents(size: Int, shimmerInstance: Shimmer = rememberShimmer(ShimmerBounds.View)) {
  Column {
    repeat(size) {
      ListItem(
        colors = ListItemDefaults.colors(containerColor = AppTheme.colors.backgroundSecondary),
        leadingContent = {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(color = AppTheme.colors.contendTertiary, shape = CircleShape)
              .shimmer(shimmerInstance),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundPrimary)
            )
          }
        },
        headlineContent = {
          Box(
            modifier = Modifier
              .background(color = AppTheme.colors.contendTertiary, shape = RoundedCornerShape(8.dp))
              .shimmer(shimmerInstance)
          ) {
            Text(
              modifier = Modifier
                .shimmer(shimmerInstance)
                .background(color = AppTheme.colors.backgroundPrimary, shape = RoundedCornerShape(5.dp))
                .width(250.dp),
              text = "",
              style = AppTheme.typography.body2,
            )
          }
        },
        supportingContent = {
          Column {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
              modifier = Modifier
                .background(color = AppTheme.colors.contendTertiary, shape = RoundedCornerShape(8.dp))
                .shimmer(shimmerInstance)
            ) {
              Text(
                modifier = Modifier
                  .shimmer(shimmerInstance)
                  .background(color = AppTheme.colors.backgroundPrimary, shape = RoundedCornerShape(5.dp))
                  .width(100.dp),
                text = "",
                style = AppTheme.typography.body2,
              )
            }
          }
        }
      )
    }
  }
}

@Composable
private fun ErrorLoadingPartContent(modifier: Modifier = Modifier, onClickRefresh: () -> Unit) {
  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      color = AppTheme.colors.textPrimary,
      text = stringResource(R.string.something_went_wrong),
      textAlign = TextAlign.Center,
      style = AppTheme.typography.subtitle
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = stringResource(R.string.failed_load_part_content),
      textAlign = TextAlign.Center,
      style = AppTheme.typography.body2,
      color = AppTheme.colors.textTertiary
    )
    TextButton(onClick = onClickRefresh) {
      Text(text = stringResource(R.string.refresh))
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ErrorLoadingPartContentPreview() {
  AppTheme {
    ErrorLoadingPartContent(onClickRefresh = {})
  }
}
