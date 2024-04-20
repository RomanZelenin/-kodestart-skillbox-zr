package com.romanzelenin.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import ru.kode.base.core.util.randomUuid
import ru.kode.base.internship.core.domain.entity.LceState

@Immutable
data class ProductsHomeViewState(
  val accountsLceState: LceState = LceState.Loading,
  val depositsLceState: LceState = LceState.Loading,
  val loadedAccounts: Map<Account, List<Card>> = emptyMap(),
  val loadedDeposits: List<Deposit> = emptyList(),
  val errorMessage: String? = null,
  val isRefreshing: Boolean = false
)

@Immutable
data class Deposit(
  val id: Id = Id(randomUuid()),
  val title: String,
  val amount: String,
  val sign: CurrencySigns,
  val rate: String,
  val date: String
)

@Immutable
data class Account(
  val id: Id = Id(randomUuid()),
  val title: String,
  val amount: String,
  val sign: CurrencySigns
)

@Immutable
data class Card(
  val id: Id = Id(randomUuid()),
  val title: String,
  val status: CardStatus,
  @DrawableRes val icon: Int
)

@JvmInline
value class Id(val value: String)

enum class CurrencySigns(val code: String) {
  RUB("₽"),
  EUR("€"),
  USD("\$")
}

enum class CardStatus(val title: String) {
  PHYSICAL("Физическая"),
  BLOCKED("Заблокирована"),
  VIRTUAL("Виртуальная")
}
