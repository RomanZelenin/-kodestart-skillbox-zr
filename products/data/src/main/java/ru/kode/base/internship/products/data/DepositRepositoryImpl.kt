package ru.kode.base.internship.products.data

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.domain.DepositRepository
import ru.kode.base.internship.products.domain.entity.CurrencySign
import ru.kode.base.internship.products.domain.entity.Deposit
import ru.kode.base.internship.products.domain.entity.DepositTerms
import javax.inject.Inject
import kotlin.random.Random

@ContributesBinding(AppScope::class)
class DepositRepositoryImpl @Inject constructor() : DepositRepository {
  override val deposits: Flow<List<Deposit>>
    get() { return flow { emit(getDeposits()) } }

  override fun term(id: Deposit.Id): Flow<DepositTerms> {
    return flow { emit(getTerm(id)) }
  }

  override suspend fun getDeposits(): List<Deposit> {
    delay(2000)
    return if (Random.nextBoolean()) {
      mockDeposits
    } else {
      throw Exception("Error loading")
    }
  }

  override suspend fun getTerm(id: Deposit.Id): DepositTerms {
    delay(1000)
    return if (Random.nextBoolean()) {
      mockTerms.first { it.id == mockDeposits.first { it.id == id }.idTerm }
    } else {
      throw Exception("Error loading")
    }
  }

  private val mockDeposits = listOf(
    Deposit(
      title = "Мой вклад",
      amount = "1 515 000,78",
      sign = CurrencySign.RUB,
      idTerm = DepositTerms.Id("1")
    ),
    Deposit(
      title = "Мой вклад2",
      amount = "100,00",
      sign = CurrencySign.USD,
      idTerm = DepositTerms.Id("2")
    ),
  )

  private val mockTerms = listOf(
    DepositTerms(
      id = DepositTerms.Id("1"),
      rate = "7.65",
      date = "31.08.2024"
    ),
    DepositTerms(
      id = DepositTerms.Id("2"),
      rate = "10.0",
      date = "01.05.2025"
    ),
  )
}