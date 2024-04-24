package ru.kode.base.internship.products.ui.home

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.ui.R
import kotlin.random.Random

object ProductsHomeMocks {
  val accounts = MutableStateFlow<Map<Account, List<Card>>>(emptyMap())
  val deposits = MutableStateFlow<List<Deposit>>(emptyList())
  val isRefreshing = MutableStateFlow(false)

  val accountState = MutableStateFlow<LceState>(LceState.None)
  val depositState = MutableStateFlow<LceState>(LceState.None)

  suspend fun fetchDeposits() {
    depositState.value = LceState.Loading
    delay(2000)
    if (Random.nextBoolean()) {
      deposits.value = mockDeposits
      depositState.value = LceState.Content
    } else {
      depositState.value = LceState.Error("Failed to load data")
    }
  }

  suspend fun fetchAccounts() {
    accountState.value = LceState.Loading
    delay(3000)
    if (Random.nextBoolean()) {
      accounts.value = mockAccounts
      accountState.value = LceState.Content
    } else {
      accountState.value = LceState.Error("Failed to load data")
    }
  }

  suspend fun fetchMockData() = coroutineScope {
    isRefreshing.value = true

    val j1 = launch {
      fetchAccounts()
    }

    val j2 = launch {
      fetchDeposits()
    }

    j1.join()
    j2.join()
    isRefreshing.value = false
  }

  private val mockDeposits = listOf(
    Deposit(
      title = "Мой вклад",
      amount = "1 515 000,78",
      sign = CurrencySigns.RUB,
      rate = "7.65",
      date = "31.08.2024"
    ),
    Deposit(
      title = "Мой вклад2",
      amount = "100,00",
      sign = CurrencySigns.USD,
      rate = "7.65",
      date = "31.08.2024"
    ),
    Deposit(
      title = "Мой вклад",
      amount = "1 515 000,78",
      sign = CurrencySigns.RUB,
      rate = "7.65",
      date = "31.08.2024"
    ),
    Deposit(
      title = "Мой вклад2",
      amount = "100,00",
      sign = CurrencySigns.USD,
      rate = "7.65",
      date = "31.08.2024"
    )
  )

  private val mockAccounts = mapOf(
    Account(title = "Счет расчетный", money = Money(amount = "457 100,00", sign = CurrencySigns.RUB)) to
      listOf(
        Card(
          title = "Карта зарплатная",
          type = CardType.PHYSICAL,
          status = CardStatus.ACTIVE,
          icon = R.drawable.ic_master_card
        ),
        Card(
          title = "Дополнительная карта",
          type = CardType.VIRTUAL,
          status = CardStatus.BLOCKED,
          icon = R.drawable.ic_visa_card
        ),
        Card(
          title = "Дополнительная карта",
          status = CardStatus.ACTIVE,
          type = CardType.VIRTUAL,
          icon = R.drawable.ic_visa_card
        )
      ),
    Account(title = "Счет расчетный2", money = Money(amount = "457 100,00", sign = CurrencySigns.EUR)) to
      listOf(
        Card(
          title = "Карта зарплатная",
          type = CardType.PHYSICAL,
          status = CardStatus.ACTIVE,
          icon = R.drawable.ic_master_card
        ),
        Card(
          title = "Дополнительная карта",
          type = CardType.VIRTUAL,
          status = CardStatus.BLOCKED,
          icon = R.drawable.ic_visa_card
        ),
        Card(
          title = "Дополнительная карта",
          status = CardStatus.ACTIVE,
          type = CardType.VIRTUAL,
          icon = R.drawable.ic_visa_card
        )
      ),
    Account(title = "Счет расчетный3", money = Money(amount = "457 100,00", sign = CurrencySigns.USD)) to
      listOf(
        Card(
          title = "Карта зарплатная",
          type = CardType.PHYSICAL,
          status = CardStatus.BLOCKED,
          icon = R.drawable.ic_master_card
        ),
        Card(
          title = "Дополнительная карта",
          type = CardType.VIRTUAL,
          status = CardStatus.BLOCKED,
          icon = R.drawable.ic_visa_card
        ),
        Card(
          title = "Дополнительная карта",
          status = CardStatus.ACTIVE,
          type = CardType.VIRTUAL,
          icon = R.drawable.ic_visa_card
        )
      ),
  )
}
