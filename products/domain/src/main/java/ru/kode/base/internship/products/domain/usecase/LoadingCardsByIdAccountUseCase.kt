package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
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
class LoadingCardsByIdAccountUseCase @Inject constructor(
  private val cardRepository: CardRepository,
  private val accountsRepository: AccountsRepository,
) :
  BaseUseCase<LoadingCardsByIdAccountUseCase.State>(State()) {
  data class State(
    val cards: MutableMap<Account.Id, Pair<LceState, List<Card>>> = mutableMapOf(),
  )

  val cards: Flow<Map<Account.Id, Pair<LceState, List<Card>>>>
    get() = stateFlow.map { it.cards }.distinctUntilChanged()


  suspend operator fun invoke(accountId: Account.Id, cardsId: List<Card.Id>) {
    loadAccountCards(accountId, cardsId)
  }

  private suspend fun loadAccountCards(accountId: Account.Id, cardsId: List<Card.Id>) {
    setState {
      cards[accountId] = LceState.Loading to emptyList()
      copy(cards = cards)
    }
    val listCards = mutableListOf<Card>()
    cardsId.forEach { listCards.add(cardRepository.cardDetails(it).first()) }
    setState {
      cards[accountId] = LceState.Content to listCards
      copy(cards = cards)
    }
  }
}