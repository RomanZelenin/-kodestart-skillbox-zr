package ru.kode.base.internship.products.ui.carddetails

import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card

class CardDetailsIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val loadCardInfo = intent<Card.Id>(name = "loadCardInfo")
  val loadAccount = intent<Account.Id>(name = "loadAccount")
  val showRenameCardDialog = intent<Boolean>(name = "renameCardDialog")
  val saveCardName = intent<String>(name = "saveCardName")
  val dismissSnackbar = intent(name = "dismissSnackbar")
}
