package ru.kode.base.internship.products.ui.home

import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.products.domain.entity.Account
import ru.kode.base.internship.products.domain.entity.Card

class ProductsHomeIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val loadAccounts = intent(name = "loadAccounts")
  val loadDeposits = intent(name = "loadDeposits")
  val refresh = intent(name = "refresh")
  val expandAccount = intent<Pair<Account.Id,List<Card.Id>>>(name = "expandAccount")
  val showCardDetails = intent<Card.Id>(name = "showCardDetails")
}
