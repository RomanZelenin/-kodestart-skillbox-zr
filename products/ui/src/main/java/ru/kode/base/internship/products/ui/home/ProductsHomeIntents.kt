package ru.kode.base.internship.products.ui.home

import ru.kode.base.core.BaseViewIntents

class ProductsHomeIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val loadAccounts = intent(name = "loadAccounts")
  val loadDeposits = intent(name = "loadDeposits")
  val refresh = intent(name = "refresh")
  val expandAccount = intent<Account.Id>(name = "expandAccount")
}
