package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.CardRepository
import ru.kode.base.internship.products.domain.entity.Card
import javax.inject.Inject

@SingleIn(AppScope::class)
class RenameCardUseCase @Inject constructor(
  private val repository: CardRepository,
) : BaseUseCase<RenameCardUseCase.State>(State()) {

  data class State(
    val cardState: LceState = LceState.None,
  )

  val cardState: Flow<LceState>
    get() = stateFlow.map { it.cardState }.distinctUntilChanged()

  suspend operator fun invoke(cardId: Card.Id, newName: String) {
    setState { copy(cardState = LceState.Loading) }
    try {
      repository.editCardName(id = cardId, name = newName)
      setState { copy(cardState = LceState.Content) }
    } catch (e: Exception) {
      setState { copy(cardState = LceState.Error(e.message)) }
    }
  }
}