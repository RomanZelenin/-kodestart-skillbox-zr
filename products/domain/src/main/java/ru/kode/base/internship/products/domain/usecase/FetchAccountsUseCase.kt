package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.AccountsRepository
import ru.kode.base.internship.products.domain.CardRepository
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import javax.inject.Inject

@SingleIn(AppScope::class)
class FetchAccountsUseCase @Inject constructor(
  private val accountsRepository: AccountsRepository,
  private val cardRepository: CardRepository,
) : BaseUseCase<FetchAccountsUseCase.State>(State()) {
  data class State(
    val accountState: LceState = LceState.None,
    val accounts: MutableMap<Account,List<Card>> = mutableMapOf()
  )

  suspend operator fun invoke() {
    fetchAccounts()
  }

  val accountState: Flow<LceState>
    get() = stateFlow.map { it.accountState }.distinctUntilChanged()

  val accounts: Flow<Map<Account,List<Card>>>
    get() = stateFlow.map { it.accounts }.distinctUntilChanged()

  private suspend fun fetchAccounts() {
    setState { copy(accountState = LceState.Loading) }
    try {
      val loadedAccounts = accountsRepository.getAccounts()
      val result = mutableMapOf<Account, List<Card>>()
      loadedAccounts.forEach {
        val cards = mutableListOf<Card>()
        it.attachedCards.fold(cards) { acc, id ->
          acc.apply { add(cardRepository.getCardDetails(id)) }
        }
        result[it] = cards
      }
      setState { copy(accountState = LceState.Content, accounts = result) }
    } catch (e: Exception) {
      setState { copy(accountState = LceState.Error(e.message)) }
    }
  }
}