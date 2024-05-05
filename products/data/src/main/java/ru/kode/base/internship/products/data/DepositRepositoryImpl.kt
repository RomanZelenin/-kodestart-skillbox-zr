package ru.kode.base.internship.products.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.data.network.ProductsApi
import ru.kode.base.internship.products.data.storage.ProductsDatabase
import ru.kode.base.internship.products.domain.DepositRepository
import ru.kode.base.internship.products.domain.entity.CurrencySign
import ru.kode.base.internship.products.domain.entity.Deposit
import ru.kode.base.internship.products.domain.entity.DepositTerms
import ru.kode.base.internship.products.domain.entity.Money
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DepositRepositoryImpl @Inject constructor(
  private val productsApi: ProductsApi,
  private val productsDatabase: ProductsDatabase,
) : DepositRepository {
  override val deposits: Flow<List<Deposit>>
    get() = productsDatabase.depositQueries.selectAll { id, name, balance, currency, status, closeDate, rate ->
      Deposit(
        id = Deposit.Id(id.toString()),
        title = name,
        money = Money(amount = balance, sign = currency.toCurrencySign()),
        term = DepositTerms(rate = rate, date = closeDate)
      )
    }
      .asFlow()
      .mapToList(Dispatchers.IO)


  override suspend fun fetchDeposits() {
    val deposits = productsApi.getDeposits().deposits.map {
      val depositWithTerms =
        productsApi.getDeposit(id = it.depositId.toString(), prefer = "code=200, example=android-${it.depositId}")
      products.data.account.Deposit(
        id = it.depositId.toLong(),
        name = depositWithTerms.name,
        balance = depositWithTerms.balance.toString(),
        currency = depositWithTerms.currency,
        status = depositWithTerms.status,
        closeDate = depositWithTerms.closeDate,
        rate = depositWithTerms.rate.toString()
      )
    }.toList()

    productsDatabase.depositQueries.transaction {
      productsDatabase.depositQueries.deleteAll()
      deposits.forEach { productsDatabase.depositQueries.insertDepositObject(it) }
    }
  }
}