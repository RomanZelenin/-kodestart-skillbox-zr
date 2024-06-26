package ru.kode.base.internship.products.ui.home

import androidx.compose.runtime.Immutable
import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card

class ProductsHomeIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val loadAccounts = intent(name = "loadAccounts")
  val loadDeposits = intent(name = "loadDeposits")
  val refresh = intent(name = "refresh")
  val expandAccount = intent<Account.Id>(name = "expandAccount")
  val showCardDetails = intent<AccountIdWithCardId>(name = "showCardDetails")
}

@Immutable
data class AccountIdWithCardId(val accountId: Account.Id, val cardId: Card.Id)
