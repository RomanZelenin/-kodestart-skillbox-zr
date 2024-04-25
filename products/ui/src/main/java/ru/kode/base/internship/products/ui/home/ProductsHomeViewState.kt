package ru.kode.base.internship.products.ui.home

import androidx.compose.runtime.Immutable
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.Deposit
import ru.kode.base.internship.products.domain.entity.DepositTerms

@Immutable
data class ProductsHomeViewState(
  val accountsLceState: LceState = LceState.Loading,
  val depositsLceState: LceState = LceState.Loading,
  val loadedAccounts: List<Account> = emptyList(),
  val loadedDeposits: List<Deposit> = emptyList(),
  val terms: List<DepositTerms> = emptyList(),
  val errorMessage: String? = null,
  val isRefreshing: Boolean = false,
  val listExpandedAccounts: List<Account.Id> = emptyList(),
  val listCards: Map<Account.Id, Pair<LceState, List<Card>>> = emptyMap(),
)

