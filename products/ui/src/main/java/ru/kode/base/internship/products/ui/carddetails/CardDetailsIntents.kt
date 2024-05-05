package ru.kode.base.internship.products.ui.carddetails

import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card

class CardDetailsIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val showRenameCardDialog = intent<Boolean>(name = "renameCardDialog")
  val saveCardName = intent<String>(name = "saveCardName")
  val dismissNotification = intent(name = "dismissNotification")
  val loadCards = intent<Account.Id>(name = "loadCards")
  val setCurrentCard = intent<Card.Id>(name = "setCurrentCard")
}
