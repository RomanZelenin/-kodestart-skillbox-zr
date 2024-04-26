package ru.kode.base.internship.products.ui.carddetails

import androidx.compose.runtime.Immutable
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.ui.carddetails.entity.CardDetailsErrorMessage

@Immutable
data class CardDetailsViewState(
  val currentCard: Card? = null,
  val account: Account? = null,
  val isShowRenameCardDialog: Boolean = false,
  val errorMessage: CardDetailsErrorMessage? = null,
  val isShowSnackbar: Boolean = false,
)
