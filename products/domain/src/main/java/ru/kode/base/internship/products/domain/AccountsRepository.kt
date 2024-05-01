package ru.kode.base.internship.products.domain

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.entity.Account

interface AccountsRepository {

  val accounts: Flow<List<Account>>

  suspend fun fetchAccounts()

}
