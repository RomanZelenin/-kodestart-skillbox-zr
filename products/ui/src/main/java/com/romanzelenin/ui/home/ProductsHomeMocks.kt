package com.romanzelenin.ui.home

import com.romanzelenin.ui.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kode.base.internship.core.domain.entity.LceState
import kotlin.random.Random

object ProductsHomeMocks {
  val accounts = MutableStateFlow<Map<Account, List<Card>>>(emptyMap())
  val deposits = MutableStateFlow<List<Deposit>>(emptyList())
  val isRefreshing = MutableStateFlow<Boolean>(false)

  val accountState = MutableStateFlow<LceState>(LceState.None)
  val depositState = MutableStateFlow<LceState>(LceState.None)

  suspend fun fetchDeposits() {
    depositState.value = LceState.Loading
    delay(2000)
    if (Random.nextBoolean()) {
      deposits.value = listOf(
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
      depositState.value = LceState.Content
    } else {
      depositState.value = LceState.Error("Failed to load data")
    }
  }

  suspend fun fetchAccounts(){
    accountState.value = LceState.Loading
    delay(3000)
    if (Random.nextBoolean()) {
      accounts.value = mapOf(
        Account(title = "Счет расчетный", amount = "457 100,00", sign = CurrencySigns.RUB) to
          listOf(
            com.romanzelenin.ui.home.Card(
              title = "Карта зарплатная",
              status = CardStatus.PHYSICAL,
              icon = R.drawable.ic_master_card
            ),
            com.romanzelenin.ui.home.Card(
              title = "Дополнительная карта",
              status = CardStatus.BLOCKED,
              icon = R.drawable.ic_visa_card
            ),
            com.romanzelenin.ui.home.Card(
              title = "Дополнительная карта",
              status = CardStatus.VIRTUAL,
              icon = R.drawable.ic_visa_card
            )
          ),
        Account(title = "Счет расчетный2", amount = "457 100,00", sign = CurrencySigns.EUR) to
          listOf(
            com.romanzelenin.ui.home.Card(
              title = "Карта зарплатная",
              status = CardStatus.PHYSICAL,
              icon = R.drawable.ic_master_card
            ),
            com.romanzelenin.ui.home.Card(
              title = "Дополнительная карта",
              status = CardStatus.BLOCKED,
              icon = R.drawable.ic_visa_card
            ),
            com.romanzelenin.ui.home.Card(
              title = "Дополнительная карта",
              status = CardStatus.VIRTUAL,
              icon = R.drawable.ic_visa_card
            )
          ),
        Account(title = "Счет расчетный3", amount = "457 100,00", sign = CurrencySigns.USD) to
          listOf(
            com.romanzelenin.ui.home.Card(
              title = "Карта зарплатная1",
              status = CardStatus.PHYSICAL,
              icon = R.drawable.ic_master_card
            ),
            com.romanzelenin.ui.home.Card(
              title = "Дополнительная карта2",
              status = CardStatus.BLOCKED,
              icon = R.drawable.ic_visa_card
            ),
            com.romanzelenin.ui.home.Card(
              title = "Дополнительная карта3",
              status = CardStatus.VIRTUAL,
              icon = R.drawable.ic_visa_card
            )
          ),
      )
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
}

