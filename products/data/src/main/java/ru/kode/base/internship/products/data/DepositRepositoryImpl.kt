package ru.kode.base.internship.products.data

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.domain.DepositRepository
import ru.kode.base.internship.products.domain.entity.CurrencySigns
import ru.kode.base.internship.products.domain.entity.Deposit
import ru.kode.base.internship.products.domain.entity.DepositTerms
import javax.inject.Inject
import kotlin.random.Random

@ContributesBinding(AppScope::class)
class DepositRepositoryImpl @Inject constructor() : DepositRepository {
  override val deposits: Flow<List<Deposit>>
    get() = flowOf(mockDeposits.shuffled().subList(0, Random.nextInt(1, mockDeposits.size)))

  override fun term(id: Deposit.Id): Flow<DepositTerms> {
    return flow {
      emit(mockTerms.first { it.id == mockDeposits.first { it.id == id }.idTerm })
    }
  }

  private val mockDeposits = listOf(
    Deposit(
      title = "Мой вклад",
      amount = "1 515 000,78",
      sign = CurrencySigns.RUB,
      idTerm = DepositTerms.Id("1")
    ),
    Deposit(
      title = "Мой вклад2",
      amount = "100,00",
      sign = CurrencySigns.USD,
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