package ru.kode.base.internship.products.data

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.domain.AccountsRepository
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.CurrencySigns
import ru.kode.base.internship.products.domain.entity.Money
import javax.inject.Inject
import kotlin.random.Random

@ContributesBinding(AppScope::class)
class AccountsRepositoryImpl @Inject constructor() : AccountsRepository {
  override val accounts: Flow<List<Account>>
    get() {
    return  flow {
        emit(mockAccounts.shuffled().subList(0,Random.nextInt(1,mockAccounts.size)))
      }
    }

  private val mockAccounts = listOf(
    Account(
      id = Account.Id("1"),
      title = "Счет расчетный",
      money = Money(amount = "457 100,00", sign = CurrencySigns.RUB),
      attachedCards = listOf(Card.Id("1"), Card.Id("2"), Card.Id("3"))
    ),
    Account(
      id = Account.Id("2"),
      title = "Счет расчетный2",
      money = Money(amount = "457 100,00", sign = CurrencySigns.EUR),
      attachedCards = listOf(Card.Id("2"), Card.Id("3"))
    ),
    Account(
      id = Account.Id("3"),
      title = "Счет расчетный3",
      money = Money(amount = "457 100,00", sign = CurrencySigns.USD),
      attachedCards = listOf(Card.Id("1"))
    )
  )
}