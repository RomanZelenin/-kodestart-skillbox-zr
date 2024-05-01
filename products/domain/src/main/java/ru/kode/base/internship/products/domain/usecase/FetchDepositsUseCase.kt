package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.DepositRepository
import ru.kode.base.internship.products.domain.entity.Deposit
import ru.kode.base.internship.products.domain.entity.DepositTerms
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@SingleIn(AppScope::class)
class FetchDepositsUseCase @Inject constructor(
  private val depositRepository: DepositRepository,
) : BaseUseCase<FetchDepositsUseCase.State>(State()) {
  data class State(
    val depositState: LceState = LceState.None
  )

  suspend operator fun invoke() {
    fetchDeposits()
  }

  val depositState: Flow<LceState>
    get() = stateFlow.map { it.depositState }.distinctUntilChanged()

  val deposits: Flow<List<Deposit>>
    get() = depositRepository.deposits


  private suspend fun fetchDeposits() {
    setState { copy(depositState = LceState.Loading) }
    try {
      depositRepository.fetchDeposits()
      setState { copy(depositState = LceState.Content) }
    } catch (e: Exception) {
      setState { copy(depositState = LceState.Error(e.message)) }
    }
  }

}