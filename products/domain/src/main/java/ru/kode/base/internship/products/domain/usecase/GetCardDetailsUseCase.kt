package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.entity.Account
import javax.inject.Inject

@SingleIn(AppScope::class)
class GetCardDetailsUseCase @Inject constructor(
  private val accountsUseCase: FetchAccountsUseCase,
) :
  BaseUseCase<GetCardDetailsUseCase.State>(State()) {
  data class State(
    val cardsState: LceState = LceState.None,
    val account: Flow<Account?> = flowOf(null),
  )

  val account: Flow<Account?>
    get() = stateFlow.map{ it.account }.flatMapLatest { it }


  suspend fun loadAccountCards(accountId: Account.Id) {
    setState { copy(cardsState = LceState.Loading) }
    try {
      val account = accountsUseCase.accounts.map { it.firstOrNull { it.id == accountId }}
      setState { copy(cardsState = LceState.Content, account = account) }
    } catch (e: Exception) {
      setState { copy(cardsState = LceState.Error(e.message)) }
    }
  }

}
