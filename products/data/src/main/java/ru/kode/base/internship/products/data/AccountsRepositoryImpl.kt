package ru.kode.base.internship.products.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.data.network.ProductsApi
import ru.kode.base.internship.products.data.network.entity.PathAccounts
import ru.kode.base.internship.products.data.storage.ProductsDatabase
import ru.kode.base.internship.products.domain.AccountsRepository
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.CardStatus
import ru.kode.base.internship.products.domain.entity.CardType
import ru.kode.base.internship.products.domain.entity.CurrencySign
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
    loadData()
  }


  private suspend fun loadData() {
    productsDatabase.cardQueries.deleteAll()
    productsDatabase.accountQueries.deleteAll()
    productsApi.getAccounts().accounts.forEach { account ->
      productsDatabase.accountQueries.insertAccountObject(account.toStorageModel())
      account.cards.forEach { card ->
        productsDatabase.cardQueries.insertCardObject(
          card.toStorageModel(
            accountId = account.accountId.toLong(),
            expiredAt = productsApi.getCard(
              id = card.card_id,
              prefer = "code=200, example=android-${card.card_id}"
            ).expiredAt
          )
        )
      }
    }
  }
}


