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
class FetchCardsUseCase @Inject constructor(
  private val cardRepository: CardRepository,
  private val accountsRepository: AccountsRepository,
) :
  BaseUseCase<FetchCardsUseCase.State>(State()) {
  data class State(
    val cardsState: LceState = LceState.None,
    val cards: MutableMap<Account.Id, List<Card>> = mutableMapOf(),
  )

  val cards: Flow<Map<Account.Id, List<Card>>>
    get() = stateFlow.map { it.cards }.distinctUntilChanged()

    val cardsState: Flow<LceState>
      get() = stateFlow.map { it.cardsState }.distinctUntilChanged()

  suspend operator fun invoke() {
    loadAccountsCards()
  }

  private suspend fun loadAccountsCards() {
    setState { copy(cardsState = LceState.Loading) }
    try {
      val accounts = accountsRepository.getAccounts()
      val res = mutableMapOf<Account.Id, List<Card>>()
      accounts.forEach {
        val cards = mutableListOf<Card>()
        it.attachedCards.fold(cards) { acc, id ->
          acc.apply { add(cardRepository.getCardDetails(id)) }
        }
        res[it.id] = cards
      }
      setState { copy(cardsState = LceState.Content, cards = res) }
    } catch (e: Exception) {
      setState { copy(cardsState = LceState.Error(e.message)) }
    }
  }
}