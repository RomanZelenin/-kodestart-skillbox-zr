package ru.kode.base.internship.products.ui.home

import androidx.compose.runtime.Immutable
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Deposit

@Immutable
data class ProductsHomeViewState(
  val accountsLceState: LceState = LceState.Loading,
  val depositsLceState: LceState = LceState.Loading,
  val accounts: List<Account> = emptyList(),
  val deposits: List<Deposit> = emptyList(),
  val isRefreshing: Boolean = false,
  val expandedAccountIds: List<Account.Id> = emptyList(),
)

