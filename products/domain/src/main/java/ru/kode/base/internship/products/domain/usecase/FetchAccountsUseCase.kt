package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.AccountsRepository
import ru.kode.base.internship.products.domain.entity.Account
import javax.inject.Inject

@SingleIn(AppScope::class)
class FetchAccountsUseCase @Inject constructor(
  private val accountsRepository: AccountsRepository,
) : BaseUseCase<FetchAccountsUseCase.State>(State()) {
  data class State(
    val accountState: LceState = LceState.None,
  )

  suspend operator fun invoke() {
    fetchAccounts()
  }

  val accountState: Flow<LceState>
    get() = stateFlow.map { it.accountState }.distinctUntilChanged()

  val accounts: Flow<List<Account>>
    get() = accountsRepository.accounts.distinctUntilChanged()

  private suspend fun fetchAccounts() {
   setState { copy(accountState = LceState.Loading) }
    try {
      accountsRepository.fetchAccounts()
      setState { copy(accountState = LceState.Content) }
    } catch (e: Exception) {
      setState { copy(accountState = LceState.Error(e.message)) }
    }
  }
}