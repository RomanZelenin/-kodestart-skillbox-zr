package ru.kode.base.internship.products.ui.carddetails

import kotlinx.coroutines.flow.MutableSharedFlow
import ru.dimsuz.unicorn2.Machine
import ru.dimsuz.unicorn2.machine
import ru.kode.base.core.BaseViewModel
import ru.kode.base.internship.products.domain.usecase.GetCardByIdUseCase
import ru.kode.base.internship.routing.FlowEvent
import javax.inject.Inject

class CardDetailsViewModel @Inject constructor(
  private val flowEvents: MutableSharedFlow<FlowEvent>,
  private val getCardByIdUseCase: GetCardByIdUseCase,
) : BaseViewModel<CardDetailsViewState, CardDetailsIntents>() {
  override fun buildMachine(): Machine<CardDetailsViewState> {
    return machine {
      initial = CardDetailsViewState() to null

      onEach(intent(CardDetailsIntents::navigateOnBack)) {
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.NavigateBack) }
      }

      onEach(intent(CardDetailsIntents::loadCardInfo)) {
        action { _, _, cardId ->
            getCardByIdUseCase(cardId)
        }
      }

      onEach(getCardByIdUseCase.card){

      }
    }
  }
}