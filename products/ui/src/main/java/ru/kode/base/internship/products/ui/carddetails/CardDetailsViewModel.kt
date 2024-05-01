package ru.kode.base.internship.products.ui.carddetails

import kotlinx.coroutines.flow.MutableSharedFlow
import ru.dimsuz.unicorn2.Machine
import ru.dimsuz.unicorn2.machine
import ru.kode.base.core.BaseViewModel
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.usecase.GetCardDetailsUseCase
import ru.kode.base.internship.products.domain.usecase.RenameCardUseCase
import ru.kode.base.internship.products.ui.carddetails.entity.CardDetailsErrorMessage
import ru.kode.base.internship.routing.FlowEvent
import javax.inject.Inject

class CardDetailsViewModel @Inject constructor(
  private val flowEvents: MutableSharedFlow<FlowEvent>,
  private val getCardsDetailsUseCase: GetCardDetailsUseCase,
  private val renameCardUseCase: RenameCardUseCase,
) : BaseViewModel<CardDetailsViewState, CardDetailsIntents>() {
  override fun buildMachine(): Machine<CardDetailsViewState> {

    return machine {
      initial = CardDetailsViewState() to null

      onEach(intent(CardDetailsIntents::navigateOnBack)) {
        transitionTo { state, _ -> state.copy(isShowNotification = false) }
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.NavigateBack) }
      }

      onEach(intent(CardDetailsIntents::loadCards)) {
        transitionTo { state, _ -> state.copy(loadStateCards = LceState.Loading) }
        action { _, _, accountId -> getCardsDetailsUseCase.loadAccountCards(accountId) }
      }

      onEach(getCardsDetailsUseCase.account) {
        transitionTo { state, account ->
          if (account != null) {
            state.copy(
              loadStateCards = LceState.Content,
              cards = account.attachedCards,
              money = account.money
            )
          } else {
            state.copy(loadStateCards = LceState.Error(""))
          }
        }
      }


      onEach(intent(CardDetailsIntents::setCurrentCard)) {
        transitionTo { state, id ->
          state.copy(currentCard = id)
        }
      }


      onEach(intent(CardDetailsIntents::showRenameCardDialog)) {
        transitionTo { state, isShow ->
          state.copy(isShowCardRenaming = isShow)
        }
      }

      onEach(intent(CardDetailsIntents::saveCardName)) {
        transitionTo { state, name ->
          if (name.trim().isEmpty()) {
            state.copy(
              errorMessage = CardDetailsErrorMessage.ValidationError.EmptyCardName,
              isShowCardRenaming = false,
              isShowNotification = true
            )
          } else {
            state.copy(isShowCardRenaming = false)
          }
        }
        action { _, newState, cardName -> renameCardUseCase(cardId = newState.currentCard, newName = cardName) }
      }

      onEach(renameCardUseCase.cardState) {
        transitionTo { state, payload ->
          if (payload !is LceState.None){
            state.copy(
              errorMessage = if (payload is LceState.Error) CardDetailsErrorMessage.ValidationError.EmptyCardName else null,
              isShowNotification = true
            )
          }else{
            state
          }
        }
      }

      onEach(intent(CardDetailsIntents::dismissSnackbar)) {
        transitionTo { state, _ -> state.copy(isShowNotification = false) }
      }
    }
  }
}
