package ru.kode.base.internship.routing

import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card

sealed class FlowEvent {
  data object UserIdentificationDismissed : FlowEvent()
  data object LoginRequested : FlowEvent()
  data object EnterPasswordDismissed : FlowEvent()
  data object UserLoggedIn : FlowEvent()
  data object UserLoggedOut : FlowEvent()
  data class CardDetails(val accountId: Account.Id, val cardId: Card.Id) : FlowEvent()
  data object NavigateBack : FlowEvent()
}
