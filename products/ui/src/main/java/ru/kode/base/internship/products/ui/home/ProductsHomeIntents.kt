package ru.kode.base.internship.products.ui.home

import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.products.domain.entity.Account

class ProductsHomeIntents : BaseViewIntents() {
  val navigateOnBack = intent(name = "navigateOnBack")
  val loadAccounts = intent(name = "loadAccounts")
  val loadDeposits = intent(name = "loadDeposits")
  val refresh = intent(name = "refresh")
  val expandAccount = intent<Account.Id>(name = "expandAccount")
}
