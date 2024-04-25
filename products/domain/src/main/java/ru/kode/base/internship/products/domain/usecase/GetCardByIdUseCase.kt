package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.products.domain.CardRepository
import ru.kode.base.internship.products.domain.entity.Card
import javax.inject.Inject

@SingleIn(AppScope::class)
class GetCardByIdUseCase @Inject constructor(
  private val cardRepository: CardRepository,
) : BaseUseCase<GetCardByIdUseCase.State>(State()) {
  data class State(
    val card: Card? = null,
  )

  val card: Flow<Card?>
    get() = stateFlow.map { it.card }.distinctUntilChanged()

  suspend operator fun invoke(id: Card.Id) {
    cardRepository.cardDetails(id).collect {
      setState { copy(card = it) }
    }
  }
}