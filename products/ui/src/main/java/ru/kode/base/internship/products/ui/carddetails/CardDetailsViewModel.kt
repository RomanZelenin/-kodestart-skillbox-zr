package ru.kode.base.internship.products.ui.carddetails

import kotlinx.coroutines.flow.MutableSharedFlow
import ru.dimsuz.unicorn2.Machine
import ru.dimsuz.unicorn2.machine
import ru.kode.base.core.BaseViewModel
import ru.kode.base.internship.products.domain.usecase.GetAccountByIdUseCase
import ru.kode.base.internship.products.domain.usecase.GetCardByIdUseCase
import ru.kode.base.internship.products.ui.carddetails.entity.CardDetailsErrorMessage
import ru.kode.base.internship.routing.FlowEvent
import javax.inject.Inject

class CardDetailsViewModel @Inject constructor(
  private val flowEvents: MutableSharedFlow<FlowEvent>,
  private val getCardByIdUseCase: GetCardByIdUseCase,
  private val getAccountByIdUseCase: GetAccountByIdUseCase,
) : BaseViewModel<CardDetailsViewState, CardDetailsIntents>() {
  override fun buildMachine(): Machine<CardDetailsViewState> {
    return machine {
      initial = CardDetailsViewState() to null

      onEach(intent(CardDetailsIntents::navigateOnBack)) {
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.NavigateBack) }
      }

      onEach(intent(CardDetailsIntents::loadCardInfo)) {
        action { _, _, cardId -> getCardByIdUseCase(cardId) }
      }

      onEach(getCardByIdUseCase.card) {
        transitionTo { state, card -> state.copy(currentCard = card) }
      }

      onEach(intent(CardDetailsIntents::loadAccount)) {
        action { _, _, accountId -> getAccountByIdUseCase(accountId) }
      }

      onEach(getAccountByIdUseCase.account) {
        transitionTo { state, account -> state.copy(account = account) }
      }

      onEach(intent(CardDetailsIntents::showRenameCardDialog)) {
        transitionTo { state, isShow ->
          state.copy(isShowRenameCardDialog = isShow)
        }
      }

      onEach(intent(CardDetailsIntents::saveCardName)) {
        transitionTo { state, name ->
          if (name.isBlank()) {
            state.copy(
              errorMessage = CardDetailsErrorMessage.ValidationError.EmptyCardName,
              isShowRenameCardDialog = false,
              isShowSnackbar = true
            )
          } else {
            state.copy(
              isShowRenameCardDialog = false,
              errorMessage = null,
              currentCard = state.currentCard!!.copy(title = name),
              isShowSnackbar = true
            )
          }
        }
      }

      onEach(intent(CardDetailsIntents::dismissSnackbar)) {
        transitionTo { state, _ -> state.copy(isShowSnackbar = false) }
      }
    }
  }
}
