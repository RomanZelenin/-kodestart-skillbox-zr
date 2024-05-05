package ru.kode.base.internship.products.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.data.network.ProductsApi
import ru.kode.base.internship.products.data.storage.ProductsDatabase
import ru.kode.base.internship.products.domain.AccountsRepository
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Money
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class AccountsRepositoryImpl @Inject constructor(
  private val productsApi: ProductsApi,
  private val productsDatabase: ProductsDatabase,
) : AccountsRepository {

  override val accounts: Flow<List<Account>>
    get() = productsDatabase.accountQueries
      .selectAll { id, title, money, countryCurrency, _ ->
        Account(
          id = Account.Id(id.toString()),
          title = title,
          money = Money(
            amount = money,
            sign = countryCurrency.toCurrencySign()
          ),
          attachedCards = emptyList()
        )
      }
      .asFlow()
      .mapToList(Dispatchers.IO)
      .combine(productsDatabase.cardQueries.selectAll().asFlow().mapToList(Dispatchers.IO)) { accounts, cards ->
        accounts.map { account ->
          account.copy(attachedCards = cards.filter { it.accountId == account.id.value.toLong() }
            .map { it.toDomainModel() })
        }
      }


  override suspend fun fetchAccounts() {
    val accountsWithCards = productsApi.getAccounts().accounts.map { account ->
      account to account.cards.map {
        it.toStorageModel(
          accountId = account.accountId.toLong(),
          expiredAt = productsApi.getCard(
            id = it.card_id,
            prefer = "code=200, example=android-${it.card_id}"
          ).expiredAt
        )
      }.toList()
    }.toList()

    productsDatabase.transaction {
      productsDatabase.cardQueries.deleteAll()
      productsDatabase.accountQueries.deleteAll()
      accountsWithCards.forEach {
        val account = it.first
        val cards = it.second
        productsDatabase.accountQueries.insertAccountObject(account.toStorageModel())
        cards.forEach { card -> productsDatabase.cardQueries.insertCardObject(card) }
      }
    }
  }
}


