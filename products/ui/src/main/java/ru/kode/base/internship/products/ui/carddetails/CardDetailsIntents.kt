package ru.kode.base.internship.products.ui.carddetails

import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.products.domain.entity.Card

class CardDetailsIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val loadCardInfo = intent<Card.Id>(name = "loadCardInfo")
}