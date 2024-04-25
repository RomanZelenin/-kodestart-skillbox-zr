package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import javax.inject.Inject

@SingleIn(AppScope::class)
class FetchProductsUseCase @Inject constructor(
  private val fetchAccountsUseCase: FetchAccountsUseCase,
  private val fetchDepositUseCase: FetchDepositsUseCase,
) :
  BaseUseCase<FetchProductsUseCase.State>(State()) {
  data class State(
    val isRefreshing: Boolean = false,
  )

  val isRefreshing: Flow<Boolean>
    get() = stateFlow.map { it.isRefreshing }.distinctUntilChanged()


  suspend operator fun invoke() {
    fetchProducts()
  }

 private suspend fun fetchProducts() = coroutineScope {
    setState { copy(isRefreshing = true) }
    val j1 = launch { fetchAccountsUseCase() }
    val j2 = launch { fetchDepositUseCase() }
    j1.join()
    j2.join()
    setState { copy(isRefreshing = false) }
  }

}