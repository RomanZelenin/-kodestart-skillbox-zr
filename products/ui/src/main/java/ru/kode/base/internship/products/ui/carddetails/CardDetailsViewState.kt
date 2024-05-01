package ru.kode.base.internship.products.ui.carddetails

import androidx.compose.runtime.Immutable
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.Money
import ru.kode.base.internship.products.ui.carddetails.entity.CardDetailsErrorMessage

@Immutable
data class CardDetailsViewState(
  val currentCard: Card.Id = Card.Id("none"),
  val loadStateCards:LceState = LceState.None,
  val cards:List<Card> = emptyList(),
  val isShowCardRenaming: Boolean = false,
  val errorMessage: CardDetailsErrorMessage? = null,
  val isShowNotification: Boolean = false,
  val money: Money? = null,
)
