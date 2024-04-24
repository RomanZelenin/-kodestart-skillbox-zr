package ru.kode.base.internship.products.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.kode.base.core.util.randomUuid
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.ui.R

@Immutable
data class ProductsHomeViewState(
  val accountsLceState: LceState = LceState.Loading,
  val depositsLceState: LceState = LceState.Loading,
  val loadedAccounts: Map<Account, List<Card>> = emptyMap(),
  val loadedDeposits: List<Deposit> = emptyList(),
  val errorMessage: String? = null,
  val isRefreshing: Boolean = false,
  val listExpandedAccounts: List<Account.Id> = mutableListOf(),
)

@Immutable
data class Deposit(
  val id: Id = Id(randomUuid()),
  val title: String,
  val amount: String,
  val sign: CurrencySigns,
  val rate: String,
  val date: String,
) {
  @JvmInline
  value class Id(val value: String)
}

@Immutable
data class Account(
  val id: Id = Id(randomUuid()),
  val title: String,
  val money: Money,
) {
  @JvmInline
  value class Id(val value: String)
}

@Immutable
data class Card(
  val id: Id = Id(randomUuid()),
  val title: String,
  val status: CardStatus,
  val type: CardType,
  @DrawableRes val icon: Int,
) {
  @JvmInline
  value class Id(val value: String)
}

@Immutable
data class Money(
  val amount: String,
  val sign: CurrencySigns,
) {
  fun format(): String {
    return "$amount ${sign.code}"
  }
}

enum class CurrencySigns(val code: String) {
  RUB("₽"),
  EUR("€"),
  USD("\$")
}

enum class CardStatus(
  @StringRes val id: Int
) {
  ACTIVE(R.string.active),
  BLOCKED(R.string.blocked),
}

enum class CardType(
  @StringRes val id: Int
) {
  PHYSICAL(R.string.physical),
  VIRTUAL(R.string.virtual)
}
